package com.file.fileprocess.util;

import com.file.fileprocess.annotation.ExcelColumn;

import java.lang.reflect.Field;
import java.util.Map;

public final class ReflectionMapperUtil {

    private ReflectionMapperUtil() {}

    public static <T> T mapRow(
            Map<String, String> rowData,
            Class<T> clazz) {

        try {

            T instance =
                    clazz.getDeclaredConstructor()
                            .newInstance();

            for (Field field : clazz.getDeclaredFields()) {

                ExcelColumn annotation =
                        field.getAnnotation(
                                ExcelColumn.class);

                if (annotation == null) {
                    continue;
                }

                String value =
                        rowData.get(
                                annotation.value());

                if (value == null) {
                    continue;
                }

                field.setAccessible(true);

                setFieldValue(
                        field,
                        instance,
                        value);
            }

            return instance;

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Mapping failed", ex);
        }
    }

    private static <T> void setFieldValue(
            Field field,
            T instance,
            String value)
            throws IllegalAccessException {

        if (value == null || value.trim().isEmpty()) {
            return;
        }

        value = value.trim();

        Class<?> type = field.getType();

        try {

            if (type == String.class) {

                field.set(instance, value);

            } else if (type == Integer.class
                    || type == int.class) {

                field.set(instance,
                        Double.valueOf(value).intValue());

            } else if (type == Long.class
                    || type == long.class) {

                field.set(instance,
                        Double.valueOf(value).longValue());

            } else if (type == Double.class
                    || type == double.class) {

                field.set(instance,
                        Double.valueOf(value));

            } else if (type == Boolean.class
                    || type == boolean.class) {

                field.set(instance,
                        Boolean.parseBoolean(value));

            } else {

                field.set(instance, value);
            }

        } catch (Exception ex) {

            throw new IllegalArgumentException(
                    "Failed to convert value '"
                            + value
                            + "' to type "
                            + type.getSimpleName(),
                    ex);
        }
    }
}