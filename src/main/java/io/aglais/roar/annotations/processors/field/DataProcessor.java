package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataProcessor implements AvroProcessor<RoarData> {

    private RoarData roarDate;
    private Field field;

    @Override
    public void initialize(RoarData annotation, Field field) {
        this.roarDate = annotation;
        this.field = field;
    }

    @Override
    public Map<String, Object> getFieldProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("doc", roarDate.documentation());

        System.out.println("asdasd " + field.getName());

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
