package io.aglais.roar.annotations.processors.schema;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.avro.Schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@ToString(exclude = "parent")
public class NestedSchemaDetails {
    private NestedSchemaDetails parent;
    private final List<NestedSchemaDetails> children = new ArrayList<>();
    private Class<?> clazz;
    private Schema schema;
    private Field field;
    private boolean isPrimitiveClass;


    public void addChild(NestedSchemaDetails child){
        children.add(child);
    }

    public List<String> getAllFieldNamesOrdered(){
        List<String> allFieldNamesOrdered;

        if(parent == null){
            allFieldNamesOrdered = new ArrayList<>();
        } else {
            allFieldNamesOrdered = parent.getAllFieldNamesOrdered();
            allFieldNamesOrdered.add(field.getName());
        }

        return allFieldNamesOrdered;
    }
}
