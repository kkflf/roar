package io.aglais.roar.annotations.processors.schema;

import io.aglais.roar.annotations.processors.field.AvroProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;
import org.apache.avro.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultSchemaProcessor extends AbstractSchemaProcessor {

    private Schema schema;
    private Class<?> clazz;
    private Map<String, Object> configProperties;

    public static Field getField(Class<?> type, String fieldName) throws NoSuchFieldException {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields.stream()
                .filter(x -> x.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException("Field " + fieldName + " cannot be found in " + type.getName() + " or the super classes"));
    }

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
            NestedSchemaDetails nestedSchemaDetails = generateNestedSchemaDetails(schema, clazz);

            setFieldProperties(nestedSchemaDetails);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldProperties(NestedSchemaDetails parentNestedSchemaDetails) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        for (NestedSchemaDetails child : parentNestedSchemaDetails.getChildren()) {

            Field field = child.getField();

            for (Annotation annotation : field.getAnnotations()) {

                RoarProcessor roarProcessor = annotation.annotationType().getAnnotation(RoarProcessor.class);

                if (roarProcessor != null) {

                    AvroProcessor avroProcessor = roarProcessor.value().newInstance();

                    avroProcessor.initialize(annotation, field, child.getAllFieldNamesOrdered(), configProperties);

                    System.out.println(avroProcessor.getFullFieldName());

                    Map<String, Object> propMap = avroProcessor.getFieldProperties();

                    System.out.println("prop map: " + propMap);

                    for (Map.Entry<String, Object> entry : propMap.entrySet()) {

//                        System.out.println(schema.get + " " + field.getName());

                        child.getParent().getSchema().getField(field.getName()).addProp(entry.getKey(), entry.getValue());
                    }
                }
            }

            setFieldProperties(child);
        }
    }

    private NestedSchemaDetails generateNestedSchemaDetails(Schema schema, Class clazz) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {
        NestedSchemaDetails nestedSchemaDetails = NestedSchemaDetails.builder()
                .schema(schema)
                .clazz(clazz)
                .field(null)
                .parent(null)
                .build();


        return generateNestedSchemaDetails(nestedSchemaDetails);
    }

    private NestedSchemaDetails generateNestedSchemaDetails(NestedSchemaDetails parentNestedSchemaDetails) throws IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchFieldException {

        for (Schema.Field avroField : parentNestedSchemaDetails.getSchema().getFields()) {

            String schemaType = avroField.schema().getFullName();
            Schema schema = avroField.schema();
            String fieldName = avroField.name();
            Field field = getField(parentNestedSchemaDetails.getClazz(), fieldName);

            Class<?> clazz = field.getType();

            NestedSchemaDetails childNestedSchemaDetails = NestedSchemaDetails.builder()
                    .schema(schema)
                    .clazz(clazz)
                    .field(field)
                    .parent(parentNestedSchemaDetails)
                    .build();

            parentNestedSchemaDetails.addChild(childNestedSchemaDetails);

            if (isComplexType(schemaType)) {
                generateNestedSchemaDetails(childNestedSchemaDetails);
            }
        }

        return parentNestedSchemaDetails;
    }

    @Override
    public Schema getSchema() {
        return schema;
    }
}
