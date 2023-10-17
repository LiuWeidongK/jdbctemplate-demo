package com.example.jdbctemplatedemo.jdbc;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.joor.Reflect;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;

import lombok.extern.slf4j.Slf4j;

/**
 * @author liuweidong03
 * Created on 2023/07/24
 */
@Slf4j
public abstract class JDBCTemplateRepository<T extends JDBCTemplateEntity> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    protected abstract NamedParameterJdbcTemplate jdbcTemplate();

    protected abstract TypeReference<T> typeReference();

    public abstract String table();

    private String id() {

        String className = this.typeReference().getType().getTypeName();

        return Reflect.onClass(className)
                .create()
                .as(JDBCTemplateEntity.class)
                .id();
    }

    public long update(T object) {

        String update = "update %s set %s where %s";

        final String id = object.id();

        // to db params
        Map<String, Object> params = object.fields().entrySet().stream()
                .filter(kv -> Objects.nonNull(kv.getValue().get()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> serializeValue(object, entry)));

        String paramList = params.keySet().stream()
                .map(JDBCTemplateRepository::generateUpdateParam)
                .collect(Collectors.joining(", "));

        String where = generateUpdateParam(id);

        String sql = String.format(update, this.table(), paramList, where);
        this.jdbcTemplate().update(sql, params);
        return (long) params.get(id);
    }

    private Object serializeValue(T object, Map.Entry<String, Reflect> entry) {

        Class<?> type = entry.getValue().type();
        Object value = entry.getValue().get();

        // primitive type
        if (isJavaPrimitiveOrWrapperType(type)) {
            return value;
        }

        return value instanceof Enum ? value.toString() : toJson(value);
    }

    private String toJson (Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected static String generateUpdateParam(String field) {
        return String.format("%s = :%s", lowerCamelToLowerUnderscore(field), field);
    }

    protected static String generateBatchUpdateParam(String field) {
        return String.format("%s in (:%s)", lowerCamelToLowerUnderscore(field), field);
    }

    protected static String lowerCamelToLowerUnderscore(String lowerCamel) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, lowerCamel);
    }

    protected static boolean isJavaPrimitiveOrWrapperType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.isAssignableFrom(Byte.class)
                || clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(Integer.class)
                || clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(Float.class)
                || clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(Character.class)
                || clazz.isAssignableFrom(Boolean.class) || clazz.isAssignableFrom(String.class);
    }
}
