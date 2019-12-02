package io.aglais.roar.encoders;

import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.lang.reflect.Field;

public final class FactoryEncoder extends AdvancedCustomEncoding<Object> {

    @Override
    public void write(Object datum, Encoder out, Field field) {
        throw new RuntimeException("Do not call me!");
    }

    @Override
    public Object read(Object reuse, Decoder in, Field field) {
        throw new RuntimeException("Do not call me!");
    }
}
