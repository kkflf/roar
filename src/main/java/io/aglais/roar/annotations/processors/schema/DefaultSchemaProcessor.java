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

public class DefaultSchemaProcessor extends AbstractSchemaProcessor {


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

            Map<String, List<String>> gdprMap = setGdprSchemaProperties(nestedSchemaDetails, null, null);

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

    private Map<String, List<String>> setGdprSchemaProperties(NestedSchemaDetails parent, Map<String, List<String>> gdprMap, String gdprKey) {

        if (gdprMap == null) {
            gdprMap = new HashMap<>();
        } else {
            gdprMap = new HashMap<>(gdprMap);
        }

        //Number of keys located on parent
        Map<String, Integer> localGdprKeys = new HashMap<>();

        for (NestedSchemaDetails child : parent.getChildren()) {

            Field field = child.getField();

            Annotation annotation = field.getAnnotation(RoarGdpr.class);

                if(annotation != null){
                    RoarGdpr roarGdpr = (RoarGdpr) annotation;

                    if(roarGdpr.value() == RoarGdpr.GdprType.KEY){
                        localGdprKeys.compute(roarGdpr.key(), (key, oldValue) -> {
                            if(oldValue == null){
                                return 1;
                            } else {
                                return oldValue++;
                            }
                        });

                    }
                }

            setGdprSchemaProperties(child, gdprMap, gdprKey);
        }

        for(Map.Entry<String, Integer> entrySet : localGdprKeys.entrySet()) {
            if(entrySet.getValue() > 1){
                throw new RuntimeException("There cannot be more than 1 local key");
            }
        }


        return gdprMap;
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
