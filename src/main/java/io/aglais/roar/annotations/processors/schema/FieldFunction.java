package io.aglais.roar.annotations.processors.schema;

import io.aglais.roar.annotations.processors.field.RoarProcessor;

import java.lang.annotation.Annotation;

@FunctionalInterface
public interface FieldFunction {
    void apply(Annotation annotation, RoarProcessor roarProcessor, NestedSchemaDetails nestedSchemaDetails);
}
