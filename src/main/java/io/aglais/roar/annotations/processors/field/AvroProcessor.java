package io.aglais.roar.annotations.processors.field;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public interface AvroProcessor<A extends Annotation> {

    void initialize(A annotation, Field field, List<String> allFieldNamesOrdered, Map<String, Object> configProperties);

    Map<String, Object> getFieldProperties();

    Map<String, Object> getSchemaProperties();

    List<String> getAllFieldNamesOrdered();

    void setExtraAnnotations();

    Map<String, Object> getConfigProperties();

    String getFullFieldName();
}
