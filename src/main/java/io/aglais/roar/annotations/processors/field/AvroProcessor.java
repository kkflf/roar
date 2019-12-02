package io.aglais.roar.annotations.processors.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

public interface AvroProcessor<A extends Annotation> {

    void initialize(A annotation, Field field);

    Map<String, Object> getFieldProperties();

    Map<String, Object> getSchemaProperties();
}
