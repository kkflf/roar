package io.aglais.roar.encoders;

import io.aglais.roar.annotations.api.RoarDate;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeEncoder extends AdvancedCustomEncoding<ZonedDateTime> {

    {
        schema = Schema.create(Schema.Type.LONG);
        LogicalType logicalType = new LogicalType("myLogicalType");
        logicalType.addToSchema(schema);
    }

    @Override
    public void write(Object datum, Encoder out, Field field) throws IOException {
        out.writeLong(((ZonedDateTime) datum).toInstant().toEpochMilli());
    }

    @Override
    public ZonedDateTime read(Object reuse, Decoder in, Field field) throws IOException {
        RoarDate roarDate = field.getDeclaredAnnotation(RoarDate.class);
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(in.readLong()), ZoneId.of(roarDate.timezone()));
    }

}
