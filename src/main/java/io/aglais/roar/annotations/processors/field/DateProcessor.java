package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarDate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DateProcessor implements AvroProcessor<RoarDate> {

    private RoarDate roarData;
    private Field field;

    @Override
    public void initialize(RoarDate annotation, Field field) {
        this.roarData = annotation;
        this.field = field;
    }

    @Override
    public Map<String, Object> getFieldProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("doc", roarData.documentation());


        System.out.println("asdasd " + field.getName());

        return map;
    }

    @Override
    public Map<String, Object> getSchemaProperties() {
        return new HashMap<>();
    }


}
