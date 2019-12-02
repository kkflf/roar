package io.aglais.roar.annotations.api;


import io.aglais.roar.annotations.processors.field.DataProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RoarProcessor(DataProcessor.class)
public @interface RoarSchemaField {
    String name();
    String documentation() default "";
}
