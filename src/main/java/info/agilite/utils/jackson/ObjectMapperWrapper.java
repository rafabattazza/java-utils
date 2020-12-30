package info.agilite.utils.jackson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class ObjectMapperWrapper {

    public static final ObjectMapperWrapper INSTANCE = new ObjectMapperWrapper();

    private final ObjectMapper objectMapper;

    public ObjectMapperWrapper() {
        this.objectMapper = JSonMapperCreator.create();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public <T> T fromString(String string, Class<T> clazz) {
        try {
            return objectMapper.readValue(string, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value: " + string + " cannot be transformed to Json object", e);
        }
    }

    public <T> T fromString(String string, Type type) {
        try {
            return objectMapper.readValue(string, objectMapper.getTypeFactory().constructType(type));
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value: " + string + " cannot be transformed to Json object", e);
        }
    }

    public <T> T fromString(String string, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(string, objectMapper.getTypeFactory().constructType(typeReference));
        } catch (IOException e) {
            throw new IllegalArgumentException("The given string value: " + string + " cannot be transformed to Json object", e);
        }
    }

    
    public String toString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("The given Json object value: " + value + " cannot be transformed to a String", e);
        }
    }

    public JsonNode toJsonNode(String value) {
        try {
            return objectMapper.readTree(value);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
