package io.aglais.roar.annotations.processors.schema;

import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSchemaProcessor implements SchemaProcessor {

    private static final List<String> PRIMITIVE_TYPES = new ArrayList<>();

    protected Schema schema;
    protected Class<?> clazz;
    protected Map<String, Object> configProperties;

    public void initialize(Schema schema, Class<?> clazz, Map<String, Object> configProperties){
        this.schema = schema;
        this.clazz = clazz;
        this.configProperties = configProperties;
    }

    static {
        PRIMITIVE_TYPES.add("null");
        PRIMITIVE_TYPES.add("boolean");
        PRIMITIVE_TYPES.add("int");
        PRIMITIVE_TYPES.add("long");
        PRIMITIVE_TYPES.add("float");
        PRIMITIVE_TYPES.add("double");
        PRIMITIVE_TYPES.add("bytes");
        PRIMITIVE_TYPES.add("string");
    }

    protected boolean isComplexType(String typeName) {
        return !PRIMITIVE_TYPES.contains(typeName);
    }

    protected List<String> getPrimitiveTypes() {
        return PRIMITIVE_TYPES;
    }

}
