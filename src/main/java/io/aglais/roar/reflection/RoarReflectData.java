package io.aglais.roar.reflection;

import io.aglais.roar.annotations.api.RoarSchema;
import io.aglais.roar.annotations.processors.schema.SchemaProcessor;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoarReflectData extends ReflectData {


    private static final Map<Class<?>, Schema> CACHED_SCHEMAS = new HashMap<>();


    public synchronized Schema getSchema(Class<?> clazz) throws IllegalAccessException, InstantiationException {

        if (CACHED_SCHEMAS.containsKey(clazz)) {
            return CACHED_SCHEMAS.get(clazz);
        }

        Schema schema = ReflectData.get().getSchema(clazz);

        RoarSchema roarSchema = clazz.getAnnotation(RoarSchema.class);

        SchemaProcessor schemaProcessor = roarSchema.processor().newInstance();

        schemaProcessor.initialize(schema, clazz, getConfigProperties());

        schemaProcessor.postProcessor();

        return schema;
    }

    private Map<String, Object> getConfigProperties(){

        Map<String, Object> map = new HashMap<>();

        map.put("field-delimiter", "-");

        return map;
    }

}
