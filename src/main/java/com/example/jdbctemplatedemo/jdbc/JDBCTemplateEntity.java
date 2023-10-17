package com.example.jdbctemplatedemo.jdbc;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.joor.Reflect;

import com.google.common.base.Preconditions;

/**
 * @author liuweidong03
 * Created on 2023/07/24
 */
public interface JDBCTemplateEntity {

    default Map<String, Reflect> fields() {
        return Reflect.on(this).fields();
    }

    default String id() {

        Set<String> ids = annotationFields(ID.class);
        Preconditions.checkArgument(ids.size() == 1, "There can be only one id column.");
        return ids.stream().findFirst().get();
    }

    default Set<String> annotationFields(Class<? extends Annotation> annotation) {

        return Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(annotation))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(value = {ElementType.FIELD})
    @interface ID {

    }
}
