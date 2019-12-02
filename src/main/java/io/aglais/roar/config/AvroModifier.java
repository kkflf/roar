package io.aglais.roar.config;

import org.apache.avro.Schema;

import java.lang.reflect.Field;
import java.util.HashSet;

public class AvroModifier {

    public void removeReservedKey(Class clazz, String fieldName, String valueToRemove) throws NoSuchFieldException, IllegalAccessException {
        Field protocolReserved = clazz.getDeclaredField(fieldName);

        protocolReserved.setAccessible(true);

        HashSet<String> protocolReservedSet = (HashSet<String>) protocolReserved.get(null);

        protocolReservedSet.remove(valueToRemove);
    }

}
