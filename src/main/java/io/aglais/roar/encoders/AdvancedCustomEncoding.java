package io.aglais.roar.encoders;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.apache.avro.reflect.CustomEncoding;

import java.io.IOException;
import java.lang.reflect.Field;

public abstract class AdvancedCustomEncoding<T> extends CustomEncoding<T> {
    @Override
    protected final void write(Object datum, Encoder out) {
        throw new RuntimeException("Do not call me!");
    }

    @Override
    protected final T read(Object reuse, Decoder in) {
        throw new RuntimeException("Do not call me!");
    }

    public abstract void write(Object datum, Encoder out, Field field) throws IOException;

    public abstract T read(Object reuse, Decoder in, Field field) throws IOException;

    public T read(Decoder in, Field field) throws IOException {
        return this.read(null, in, field);
    }
}