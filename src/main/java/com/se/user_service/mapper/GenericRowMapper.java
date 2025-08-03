package com.se.user_service.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import com.se.user_service.helper.UUIDUtil;

public class GenericRowMapper<T> implements RowMapper<T>{
    private final Class<T> clazz;
    private static final Logger log = LoggerFactory.getLogger(GenericRowMapper.class);

    public GenericRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    private String toCamelCase(String snake) {
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        for (char c : snake.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                result.append(nextUpper ? Character.toUpperCase(c) : c);
                nextUpper = false;
            }
        }
        log.debug("Mapping column '{}' → '{}'", snake, result);
        return result.toString();
    }
    
    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String column = meta.getColumnLabel(i);
                Object value = rs.getObject(column);
                String fieldName = toCamelCase(column);

                log.debug("[toCamelCase] Mapping column " + fieldName);

                Field field;
                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    continue;
                }

                field.setAccessible(true);
                
                if (field.getType().equals(UUID.class) && value instanceof byte[]) {
                    field.set(obj, UUIDUtil.bytesToUuid((byte[]) value));
                } 

                else if (field.getType().equals(LocalDateTime.class)) {
                    if (value instanceof Timestamp) {
                        field.set(obj, ((Timestamp) value).toLocalDateTime());
                    } else if (value == null) {
                        field.set(obj, null);
                    } else {
                        throw new RuntimeException("Cannot convert value of type " +
                                value.getClass().getName() + " to LocalDateTime for field: " + fieldName);
                    }
                }

                else if (field.getType().equals(Boolean.class)) {
                    if (value instanceof Boolean) {
                        field.set(obj, value);
                    } else if (value instanceof Number) {
                        field.set(obj, ((Number) value).intValue() != 0);
                    } else if (value == null) {
                        field.set(obj, null);
                    } else {
                        throw new RuntimeException(
                                "Cannot convert type " + value.getClass() + " to Boolean for field: " + fieldName);
                    }
                }

                else if (field.getType().equals(Instant.class) && value instanceof Timestamp) {
                    field.set(obj, ((Timestamp) value).toInstant());
                }

                else {
                    field.set(obj, value);
                }
            }

            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map row to " + clazz.getSimpleName() + e.getMessage(), e);
        }
    }
}
