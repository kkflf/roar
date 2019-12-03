package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarData;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProcessor extends AbstractAvroProcessor<RoarData> {

    private RoarData roarDate;
    private Field field;
    private List<String> allFieldNamesOrdered;
    private Map<String, Object> configProperties;

    @Override
    public void initialize(RoarData annotation, Field field, List<String> allFieldNamesOrdered, Map<String, Object> configProperties) {
        this.roarDate = annotation;
        this.field = field;
        this.allFieldNamesOrdered = allFieldNamesOrdered;
        this.configProperties = configProperties;
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

    @Override
    public List<String> getAllFieldNamesOrdered() {
        return allFieldNamesOrdered;
    }

    public void setExtraAnnotations(){
//        if(roarDate.using() == FactoryEncoder.class){
//            TypeRuntimeAnnotations
//            field.set();
//        }
    }

    @Override
    public Map<String, Object> getConfigProperties() {
        return configProperties;
    }

}
