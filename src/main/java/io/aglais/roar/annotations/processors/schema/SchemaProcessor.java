package io.aglais.roar.annotations.processors.schema;

import org.apache.avro.Schema;

public interface SchemaProcessor {

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
