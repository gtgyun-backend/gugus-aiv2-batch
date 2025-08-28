package com.gugus.batch.auditlog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gugus.batch.auditlog.enums.AuditActionType;
import com.gugus.batch.database.entities.AuditLogs;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author : smk
 * @fileName : AuditInterceptor
 * @date : 2025. 7. 19.
 */
@Slf4j
@Component
public class AuditInterceptor implements Interceptor, ApplicationContextAware {

    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        AuditInterceptor.applicationContext = applicationContext;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, 
                               Object[] previousState, String[] propertyNames, Type[] types) {
        if (!isAuditable(entity) || previousState == null) {
            return false;
        }
        try {
            // ID를 포함한 완전한 값 추출
            Map<String, Object> oldValues = extractValuesWithId(previousState, propertyNames, entity, id);
            Map<String, Object> newValues = extractValuesWithId(currentState, propertyNames, entity, id);
            createAuditLog(entity, oldValues, newValues, AuditActionType.UPDATE, id);
        } catch (Exception e) {
            log.error("Update audit 실패: ", e);
        }
        return false;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (!isAuditable(entity)) {
            return;
        }

        try {
            Map<String, Object> oldValues = extractValuesWithId(state, propertyNames, entity, id);
            createAuditLog(entity, oldValues, null, AuditActionType.DELETE, id);
        } catch (Exception e) {
            log.error("Delete audit 실패: ", e);
        }
    }

    private boolean isAuditable(Object entity) {
        return entity.getClass().isAnnotationPresent(Auditable.class);
    }

    private boolean isRelationshipField(Object entity, String propertyName) {
        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            return field.isAnnotationPresent(OneToMany.class) ||
                   field.isAnnotationPresent(ManyToMany.class) ||
                   field.isAnnotationPresent(OneToOne.class) ||
                   field.isAnnotationPresent(ManyToOne.class);
        } catch (Exception e) {
            return false;
        }
    }

    private String getColumnName(Object entity, String propertyName) {
        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                return columnAnnotation.name();
            }
            JoinColumn joinColumnAnnotation = field.getAnnotation(JoinColumn.class);
            if (joinColumnAnnotation != null && !joinColumnAnnotation.name().isEmpty()) {
                return joinColumnAnnotation.name();
            }
        } catch (Exception e) {
            // 필드를 찾을 수 없으면 프로퍼티명 그대로 사용
        }
        return propertyName;
    }

    private Map<String, Object> extractValuesWithId(Object[] state, String[] propertyNames, Object entity, Serializable id) {
        Map<String, Object> values = new HashMap<>();
        
        Auditable auditable = entity.getClass().getAnnotation(Auditable.class);
        Set<String> excludeFields = new HashSet<>(Arrays.asList(auditable.excludeFields()));

        // 1. 먼저 ID 값 추가
        String idColumnName = getIdColumnName(entity);
        if (idColumnName != null && id != null) {
            values.put(idColumnName, id);
        }

        // 2. 다른 필드들 추가
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = propertyNames[i];
            if (excludeFields.contains(propertyName)) {
                continue;
            }
            if (isRelationshipField(entity, propertyName)) {
                continue;
            }
            String columnName = getColumnName(entity, propertyName);
            values.put(columnName, state[i]);
        }
        return values;
    }

    /**
     * 엔티티의 ID 컬럼명을 반환
     */
    private String getIdColumnName(Object entity) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation != null && !columnAnnotation.name().isEmpty()) {
                        return columnAnnotation.name();
                    }
                    return field.getName(); // 기본적으로 필드명 사용
                }
            }
        } catch (Exception e) {
            log.error("ID 컬럼명 추출 실패: ", e);
        }
        return null;
    }

    private void createAuditLog(Object entity, Map<String, Object> oldValues, 
                               Map<String, Object> newValues, AuditActionType actionType, Serializable id) {
        try {
            String tableName = getTableName(entity);
            String recordId = id != null ? id.toString() : getRecordId(entity);
            Long userNo = AuditContext.getCurrentUserNo();

            EntityManager entityManager = applicationContext.getBean(EntityManager.class);
            ObjectMapper objectMapper = applicationContext.getBean(ObjectMapper.class);

            JsonNode oldJson = oldValues != null ? objectMapper.valueToTree(oldValues) : null;
            JsonNode newJson = newValues != null ? objectMapper.valueToTree(newValues) : null;

            AuditLogs auditLog = AuditLogs.create(
                tableName, recordId, actionType,
                oldJson, newJson, userNo
            );
            
            entityManager.persist(auditLog);
            
            log.debug("Audit log 생성: {} - {} - {}", tableName, recordId, actionType);
            
        } catch (Exception e) {
            log.error("Audit log 생성 실패: ", e);
        }
    }

    private String getTableName(Object entity) {
        Auditable auditable = entity.getClass().getAnnotation(Auditable.class);
        if (!auditable.tableName().isEmpty()) {
            return auditable.tableName();
        }

        Table tableAnnotation = entity.getClass().getAnnotation(Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            return tableAnnotation.name();
        }

        return entity.getClass().getSimpleName().toLowerCase();
    }

    private String getRecordId(Object entity) {
        try {
            Field[] fields = entity.getClass().getDeclaredFields();
            List<String> idValues = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(EmbeddedId.class)) {
                    field.setAccessible(true);
                    Object value = field.get(entity);
                    if (value != null) {
                        idValues.add(value.toString());
                    }
                }
            }
            return idValues.isEmpty() ? "unknown" : String.join(",", idValues);
        } catch (Exception e) {
            log.error("RecordId 추출 실패: ", e);
            return "unknown";
        }
    }
} 