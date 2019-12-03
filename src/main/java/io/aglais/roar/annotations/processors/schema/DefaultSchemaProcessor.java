package io.aglais.roar.annotations.processors.schema;

import io.aglais.roar.annotations.processors.field.AvroProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;
import org.apache.avro.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultSchemaProcessor extends AbstractSchemaProcessor {

    private Schema schema;
    private Class<?> clazz;
    private Map<String, Object> configProperties;

    @Override
    public void initialize(Schema schema, Class<?> clazz, Map<String, Object> configProperties) {
        this.schema = schema;
        this.clazz = clazz;
        this.configProperties = configProperties;
    }

    @Override
    public void preProcessor() {

    }

    @Override
    public void postProcessor() {
        try {
            postProcessSchema(schema, clazz, new ArrayList<>());
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void postProcessSchema(Schema schema, Class clazz, List<String> parentFieldName) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (Schema.Field field : schema.getFields()) {
            if (isComplexType(field.schema().getFullName())) {
                parentFieldName.add(field.name());
                postProcessSchema(field.schema(), Class.forName(field.schema().getFullName()), parentFieldName);
            }
        }


        for (Field field : clazz.getDeclaredFields()) {

            for (Annotation annotation : field.getAnnotations()) {

                RoarProcessor roarProcessor = annotation.annotationType().getAnnotation(RoarProcessor.class);

                if (roarProcessor != null) {

                    AvroProcessor avroProcessor = roarProcessor.value().newInstance();

                    parentFieldName.add(field.getName());

                    avroProcessor.initialize(annotation, field, parentFieldName);

                    System.out.println("Field name: " + String.join(".", parentFieldName));

                    Map<String, Object> propMap = avroProcessor.getFieldProperties();

                    for (Map.Entry<String, Object> entry : propMap.entrySet()) {

//                        System.out.println(schema.get + " " + field.getName());

                        schema.getField(field.getName()).addProp(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public Schema getSchema() {
        return schema;
    }
}
