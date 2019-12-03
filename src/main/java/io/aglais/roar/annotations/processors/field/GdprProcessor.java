package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarGdpr;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdprProcessor implements AvroProcessor<RoarGdpr> {

    private RoarGdpr roarGdpr;
    private Field field;
    private List<String> allFieldNamesOrdered;

    @Override
    public void initialize(RoarGdpr annotation, Field field, List<String> allFieldNamesOrdered) {
        this.roarGdpr = annotation;
        this.field = field;
        this.allFieldNamesOrdered = allFieldNamesOrdered;
    }

    @Override
    public Map<String, Object> getFieldProperties() {
        Map<String, Object> map = new HashMap<>();


        return map;
    }

    @Override
    public Map<String, Object> getSchemaProperties() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(field.getName(), roarGdpr.value());

        return properties;
    }

    public void setExtraAnnotations(){
//        if(roarDate.using() == FactoryEncoder.class){
//            TypeRuntimeAnnotations
//            field.set();
//        }
    }

}
