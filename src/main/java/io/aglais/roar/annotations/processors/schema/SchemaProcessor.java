package io.aglais.roar.annotations.processors.schema;

import org.apache.avro.Schema;

import java.util.Map;

public interface SchemaProcessor {

    void initialize(Schema schema, Class<?> clazz, Map<String, Object> configProperties);

    /**
     * This method is called before Avro generates the schema
     */
    void preProcessor();

    /**
     * This method is called after Avro generates the schema
     */
    void postProcessor();

    Schema getSchema();

}
