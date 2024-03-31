package com.icis.demo.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icis.demo.Entity.DocumentFillable;
import com.icis.demo.Entity.DocumentStorable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DocumentUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

    public DocumentFillable createDocumentFillable(String type){
        switch (type){
            case "applicationletter":
                return new DocumentFillable();
            case "survey":
                return new DocumentFillable();
            case "companyform":
                return new DocumentFillable();
            default:
                return new DocumentFillable();
        }
    }
}
