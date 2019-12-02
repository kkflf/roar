package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarGdpr;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GdprProcessor implements AvroProcessor<RoarGdpr> {

    private RoarGdpr roarGdpr;
    private Field field;

    @Override
    public void initialize(RoarGdpr annotation, Field field) {
        this.roarGdpr = annotation;
        this.field = field;
    }

    @Override
    public Map<String, Object> getFieldProperties() {
        Map<String, Object> map = new HashMap<>();


        return map;
    }

    @Override
    public Map<String, Object> getSchemaProperties() {
        return new HashMap<>();
    }

    public void setExtraAnnotations(){
//        if(roarDate.using() == FactoryEncoder.class){
//            TypeRuntimeAnnotations
//            field.set();
//        }
    }

}
