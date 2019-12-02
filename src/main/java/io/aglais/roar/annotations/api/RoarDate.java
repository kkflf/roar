package io.aglais.roar.annotations.api;


import io.aglais.roar.annotations.processors.field.DateProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;
import io.aglais.roar.encoders.FactoryEncoder;
import org.apache.avro.reflect.CustomEncoding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RoarProcessor(DateProcessor.class)
public @interface RoarDate {
    String timezone() default "UTC";

    Class<? extends CustomEncoding<?>> using() default FactoryEncoder.class;

    String documentation() default "";
}
