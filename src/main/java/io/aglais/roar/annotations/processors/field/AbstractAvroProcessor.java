package io.aglais.roar.annotations.processors.field;

import java.lang.annotation.Annotation;

public abstract class AbstractAvroProcessor<A extends Annotation> implements AvroProcessor<A> {

    public String getFullFieldName(){
        String delimiter = getConfigProperties().get("field-delimiter").toString();

        return String.join(delimiter, getAllFieldNamesOrdered());
    }
}
