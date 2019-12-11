package io.aglais.roar.annotations.processors.field;

import io.aglais.roar.annotations.api.RoarGdpr;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdprProcessor extends AbstractAvroProcessor<RoarGdpr> {

    private RoarGdpr roarGdpr;
    private Field field;
    private List<String> allFieldNamesOrdered;
    private Map<String, Object> configProperties;

    @Override
    public void initialize(RoarGdpr annotation, Field field, List<String> allFieldNamesOrdered, Map<String, Object> configProperties) {
        this.roarGdpr = annotation;
        this.field = field;
        this.allFieldNamesOrdered = allFieldNamesOrdered;
        this.configProperties = configProperties;
    }
    @Override
    public Map<String, Object> getFieldProperties() {
        Map<String, Object> map = getCommonFieldProperties();

        map.put("gdpr_type", roarGdpr.value());

        return map;
    }

    @Override
    public Map<String, Object> getSchemaProperties() {

        Map<String, Object> properties = new HashMap<>();

        properties.put(getFullFieldName(), roarGdpr.value());

        return properties;
    }

    public RoarGdpr.GdprType getGdprType(){
        return roarGdpr.value();
    }

    @Override
    public List<String> getAllFieldNamesOrdered() {
        return allFieldNamesOrdered;
    }

    @Override
    public void setExtraAnnotations() {
//        if(roarDate.using() == FactoryEncoder.class){
//            TypeRuntimeAnnotations
//            field.set();
//        }
    }

    @Override
    public Map<String, Object> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getDocumentation() {
        return roarGdpr.documentation();
    }

}
