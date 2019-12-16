package io.aglais.roar.annotations.api;


import io.aglais.roar.annotations.processors.field.GdprProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@RoarProcessor(GdprProcessor.class)
public @interface RoarGdpr {

    //What object is this RoarGdpr used for.
    String key();

    String documentation() default "";

    GdprType value() default GdprType.NONE;

    enum GdprType {KEY, PII, NONE}
}
