package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarDate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateProcessor extends AbstractAvroProcessor<RoarDate> {

    private RoarDate roarData;
    private Field field;
    private List<String> allFieldNamesOrdered;
    private Map<String, Object> configProperties;

    @Override
    public void initialize(RoarDate annotation, Field field, List<String> allFieldNamesOrdered, Map<String, Object> configProperties) {
        this.roarData = annotation;
        this.field = field;
        this.allFieldNamesOrdered = allFieldNamesOrdered;
        this.configProperties = configProperties;
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

    @Override
    public List<String> getAllFieldNamesOrdered() {
        return allFieldNamesOrdered;
    }

    @Override
    public void setExtraAnnotations() {

    }

    @Override
    public Map<String, Object> getConfigProperties() {
        return configProperties;
    }


}
