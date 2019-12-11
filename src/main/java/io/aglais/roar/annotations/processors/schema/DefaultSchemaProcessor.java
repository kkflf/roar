package io.aglais.roar.annotations.processors.schema;

import io.aglais.roar.annotations.api.RoarGdpr;
import io.aglais.roar.annotations.processors.field.AvroProcessor;
import io.aglais.roar.annotations.processors.field.GdprProcessor;
import io.aglais.roar.annotations.processors.field.RoarProcessor;
import org.apache.avro.Schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiFunction;

public class DefaultSchemaProcessor extends AbstractSchemaProcessor {


    protected DefaultSchemaProcessor(Schema schema, Class<?> clazz, Map<String, Object> configProperties) {
        super(schema, clazz, configProperties);
    }

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
    public void preProcessor() {

    }

    @Override
    public void postProcessor() {
        try {
            NestedSchemaDetails nestedSchemaDetails = generateNestedSchemaDetails(schema, clazz);

            executeFieldFunction(nestedSchemaDetails, this::setFieldProperties);

            Map<String, RoarGdpr.GdprType> gdprMap = new HashMap<>();
            setGdprSchemaProperties(nestedSchemaDetails, gdprMap);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void executeFieldFunction(NestedSchemaDetails parent, FieldFunction function) {
        for (NestedSchemaDetails child : parent.getChildren()) {

            Field field = child.getField();

            for (Annotation annotation : field.getAnnotations()) {

                RoarProcessor roarProcessor = annotation.annotationType().getAnnotation(RoarProcessor.class);

                if (roarProcessor != null) {
                    function.apply(annotation, roarProcessor, child);
                }
            }

            executeFieldFunction(child, function);
        }
    }

    private void setGdprSchemaProperties(NestedSchemaDetails parent, Map<String, RoarGdpr.GdprType> gdprMap) {

        for (NestedSchemaDetails child : parent.getChildren()) {

            Field field = child.getField();

            for (Annotation annotation : field.getAnnotations()) {

                RoarProcessor roarProcessor = annotation.annotationType().getAnnotation(RoarProcessor.class);

                if (roarProcessor != null) {
                    try {
                        GdprProcessor gdprProcessor = (GdprProcessor) roarProcessor.value().getConstructor().newInstance();

                        gdprProcessor.initialize((RoarGdpr) annotation, field, child.getAllFieldNamesOrdered(), configProperties);

                        RoarGdpr.GdprType gdprType = gdprProcessor.getGdprType();
                        String fullFieldName = gdprProcessor.getFullFieldName();

                        gdprMap.compute(fullFieldName, (key, oldValue) -> {
                            if(oldValue == null){
                                return gdprType;
                            } else {
                                throw new RuntimeException("Gdpr violation!");
                            }
                        });
                        
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            setGdprSchemaProperties(child, gdprMap);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void setFieldProperties(Annotation annotation, RoarProcessor roarProcessor, NestedSchemaDetails child) {
        try {
            Field field = child.getField();

            AvroProcessor avroProcessor = roarProcessor.value().getConstructor().newInstance();

            avroProcessor.initialize(annotation, field, child.getAllFieldNamesOrdered(), configProperties);

            Map<String, Object> propMap = avroProcessor.getFieldProperties();

            for (Map.Entry<String, Object> entry : propMap.entrySet()) {

                child.getParent().getSchema().getField(field.getName()).addProp(entry.getKey(), entry.getValue());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }


    private NestedSchemaDetails generateNestedSchemaDetails(Schema schema, Class<?> clazz) throws NoSuchFieldException {
        NestedSchemaDetails nestedSchemaDetails = NestedSchemaDetails.builder()
                .schema(schema)
                .clazz(clazz)
                .field(null)
                .parent(null)
                .build();

        return generateNestedSchemaDetails(nestedSchemaDetails);
    }

    private NestedSchemaDetails generateNestedSchemaDetails(NestedSchemaDetails parentNestedSchemaDetails) throws NoSuchFieldException {

        for (Schema.Field avroField : parentNestedSchemaDetails.getSchema().getFields()) {

            String fieldSchemaType = avroField.schema().getFullName();
            Schema fieldSchema = avroField.schema();
            String fieldName = avroField.name();
            Field field = getField(parentNestedSchemaDetails.getClazz(), fieldName);

            Class<?> fieldClazz = field.getType();

            NestedSchemaDetails childNestedSchemaDetails = NestedSchemaDetails.builder()
                    .schema(fieldSchema)
                    .clazz(fieldClazz)
                    .field(field)
                    .parent(parentNestedSchemaDetails)
                    .build();

            parentNestedSchemaDetails.addChild(childNestedSchemaDetails);

            if (isComplexType(fieldSchemaType)) {
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
