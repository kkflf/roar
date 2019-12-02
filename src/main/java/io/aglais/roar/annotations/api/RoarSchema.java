package io.aglais.roar.annotations.api;

import io.aglais.roar.annotations.processors.schema.DefaultSchemaProcessor;
import io.aglais.roar.annotations.processors.schema.SchemaProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RoarSchema {
    String documentation() default "";

    Class<? extends SchemaProcessor> processor() default DefaultSchemaProcessor.class;
}