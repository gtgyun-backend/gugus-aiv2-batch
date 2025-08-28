package com.gugus.batch.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.gugus.batch.system.exception.BusinessException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommonUtils {
    public static <T, K> T map(K o, Class<T> cls) {
        if (o == null) {
            try {
                return cls.getConstructor().newInstance();
            } catch (Exception e) {
                withException("000", "has error");
            }
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            objectMapper.registerModule(javaTimeModule); //JavaTimeModule 등록
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(o, cls);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                return cls.getConstructor().newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    public static void withException(String errorCode) {
        throw new BusinessException(errorCode);
    }

    public static void withException(String errorCode, Map<String, String> replace) {
        throw new BusinessException(errorCode, replace);
    }

    public static void withException(String errorCode, String msg) {
        throw new BusinessException(errorCode, msg);
    }

    public static void withException(String errorCode, String msg, String language) {
        throw new BusinessException(errorCode, msg, language);
    }

    public static void withException(String errorCode, HttpStatus httpStatus) {
        throw new BusinessException(errorCode, httpStatus);
    }

    public static <T> boolean bePresent(T obj){
        if(obj instanceof String) {
            if(obj != null) ((String)obj).trim();
            return obj != null && !"".equals(obj);
        }
        if(obj instanceof Long) return obj != null;
        if(obj instanceof Integer) return obj != null;
        if(obj instanceof Map<?,?>) return obj != null && !((Map<?,?>)obj).isEmpty();
        if(obj instanceof List){
            return obj != null && !((List<?>)obj).isEmpty();
        }
        return obj != null;
    }

    /**
     * 기존 값과 새로운 값의 바뀐 키값을 추출한다.
     * @param oldValues 기존 로그값
     * @param newValues 새로운 로그값
     * @param differentKeys 추출한 키값
     */
    public void getDifferentKeys(JsonNode oldValues, JsonNode newValues, List<String> differentKeys) {
        if (oldValues == null || newValues == null) {
            return;
        }

        Set<String> allFields = new HashSet<>();
        oldValues.fieldNames().forEachRemaining(allFields::add);
        newValues.fieldNames().forEachRemaining(allFields::add);

        for (String fieldName : allFields) {
            JsonNode oldValue = oldValues.get(fieldName);
            JsonNode newValue = newValues.get(fieldName);

            if (!Objects.equals(oldValue, newValue) && !fieldName.equals("updated_by")) {
                differentKeys.add(toCamelCase(fieldName));
            }
        }
    }

    /**
     * 스네이크 케이스를 카멜 케이스로 변환
     * @param snakeCase 스네이크 케이스 문자열 (예: user_name)
     * @return 카멜 케이스 문자열 (예: userName)
     */
    public String toCamelCase(String snakeCase) {
        if (snakeCase == null || snakeCase.isEmpty()) {
            return snakeCase;
        }

        if (!snakeCase.contains("_")) {
            return snakeCase;
        }

        String[] parts = snakeCase.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0].toLowerCase());

        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                        .append(parts[i].substring(1).toLowerCase());
            }
        }

        return camelCase.toString();
    }

    public static int getSortPriority(String name) {
        if (name == null || name.isEmpty()) {
            return 4;
        }

        char firstChar = name.charAt(0);

        if (firstChar >= 'a' && firstChar <= 'z') {
            return 1; // 영문 소문자
        } else if (firstChar >= 'A' && firstChar <= 'Z') {
            return 2; // 영문 대문자
        } else if (firstChar >= '가' && firstChar <= '힣') {
            return 3; // 한글
        } else {
            return 4; // 기타
        }
    }

    /**
     * 모델명 정렬
     * 숫자(1) > 영문(2) > 한글(3) > 기타(4)
     */
    public static int getSortPriorityForModels(String name) {
        if (name == null || name.isEmpty()) {
            return 4;
        }

        char firstChar = name.charAt(0);

        if (firstChar >= '0' && firstChar <= '9') {
            return 1; // 숫자
        } else if ((firstChar >= 'a' && firstChar <= 'z') || (firstChar >= 'A' && firstChar <= 'Z')) {
            return 2; // 영문
        } else if (firstChar >= '가' && firstChar <= '힣') {
            return 3; // 한글
        } else {
            return 4; // 그 외 문자 (특수문자 등)
        }
    }

    public static String publicPathExtract(String path) {
        if(path.startsWith("/public")) {
            return path.substring(7);
        }
        return path;
    }

    /**
     * 원본 이미지 경로에서 리사이즈된 이미지 경로를 생성
     * @param originalPath 원본 이미지 경로 (예: /some/of/path/file.png)
     * @param size 리사이즈 크기 (예: 120, 480, 720)
     * @return 리사이즈된 이미지 경로 (예: /some/of/path/120/file.png)
     */
    public static String getResizedImagePath(String originalPath, int size) {
        if (originalPath == null || originalPath.isEmpty()) {
            return originalPath;
        }

        int lastSlashIndex = originalPath.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            // 경로에 '/'가 없는 경우 (파일명만 있는 경우)
            return size + "/" + originalPath;
        }

        String directory = originalPath.substring(0, lastSlashIndex);
        String fileName = originalPath.substring(lastSlashIndex + 1);

        return directory + "/" + size + "/" + fileName;
    }

    /**
     * 원본 이미지 경로에서 모든 리사이즈 크기의 경로 목록을 생성
     * @param originalPath 원본 이미지 경로
     * @return 모든 리사이즈된 이미지 경로 목록 (120, 480, 720)
     */
    public static List<String> getAllResizedImagePaths(String originalPath) {
        if (originalPath == null || originalPath.isEmpty()) {
            return List.of();
        }

        return List.of(
                getResizedImagePath(originalPath, 120),
                getResizedImagePath(originalPath, 480),
                getResizedImagePath(originalPath, 720)
        );
    }

    /**
     * 원본 이미지와 모든 리사이즈된 이미지 경로를 포함한 전체 목록을 생성
     * @param originalPath 원본 이미지 경로
     * @return 원본 + 모든 리사이즈된 이미지 경로 목록
     */
    public static List<String> getAllImagePaths(String originalPath) {
        if (originalPath == null || originalPath.isEmpty()) {
            return List.of();
        }

        List<String> allPaths = new java.util.ArrayList<>();
        allPaths.add(originalPath); // 원본 추가
        allPaths.addAll(getAllResizedImagePaths(originalPath)); // 리사이즈된 이미지들 추가

        return allPaths;
    }

    /**
     * 이름을 마스킹 처리 (첫 글자 제외하고 나머지는 '*'로 처리)
     * @param name 원본 이름
     * @return 마스킹된 이름 (예: "홍길동" -> "홍**", "John" -> "J***")
     */
    public static String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        if (name.length() == 1) {
            return name;
        }

        StringBuilder masked = new StringBuilder();
        masked.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            masked.append('*');
        }

        return masked.toString();
    }

    /**
     * 이메일을 마스킹 처리 (첫 글자 제외하고 '@' 앞까지 '*'로 처리)
     * @param email 원본 이메일
     * @return 마스킹된 이메일 (예: "test@gmail.com" -> "t***@gmail.com")
     */
    public static String maskEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return email; // '@'가 없거나 첫 번째 문자인 경우 원본 반환
        }

        String localPart = email.substring(0, atIndex);
        String domainPart = email.substring(atIndex);

        if (localPart.length() == 1) {
            return email; // 로컬 부분이 한 글자인 경우 원본 반환
        }

        StringBuilder masked = new StringBuilder();
        masked.append(localPart.charAt(0));
        for (int i = 1; i < localPart.length(); i++) {
            masked.append('*');
        }
        masked.append(domainPart);

        return masked.toString();
    }
}
