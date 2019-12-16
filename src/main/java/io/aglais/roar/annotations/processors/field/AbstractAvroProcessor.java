package io.aglais.roar.annotations.processors.field;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractAvroProcessor<A extends Annotation> implements AvroProcessor<A> {

    public String getFullFieldName() {
        String delimiter = getConfigProperties().get("field-delimiter").toString();

        return String.join(delimiter, getAllFieldNamesOrdered());
    }


    protected Map<String, Object> getCommonFieldProperties() {
        Map<String, Object> map = new HashMap<>();

        map.put("doc", getDocumentation());

        return map;
    }
}
