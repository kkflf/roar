package io.aglais.roar.annotations.processors.schema;

import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface SchemaProcessor {

    void initialize(Schema schema, Class<?> clazz, Map<String, Object> configProperties);

    void preProcessor();

    void postProcessor();

    Schema getSchema();

}
