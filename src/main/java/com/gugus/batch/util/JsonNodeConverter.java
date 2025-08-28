package com.gugus.batch.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author : smk
 * @fileName : JsonNodeConverter
 * @date : 2025. 7. 24.
 */
@Converter
public class JsonNodeConverter implements AttributeConverter<JsonNode, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("JSON 변환 실패", e);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readTree(s);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}
