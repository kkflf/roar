package io.aglais.roar.encoders;

import io.aglais.roar.annotations.api.RoarDate;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;
import org.apache.avro.reflect.CustomEncoding;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateFactoryEncoder extends AdvancedCustomEncoding<Object> {

    {
        schema = Schema.create(Schema.Type.LONG);
        LogicalType logicalType = new LogicalType("myLogicalType");
        logicalType.addToSchema(schema);
    }

    @Override
    public void write(Object datum, Encoder out, Field field) throws IOException {

        AdvancedCustomEncoding<?> encoding = null;

        if(datum instanceof ZonedDateTime){
            encoding = new ZonedDateTimeEncoder();
            encoding.write(datum, out, field);
        }
//        encoding.getLogicalType().addToSchema(schema);
    }

    @Override
    public Object read(Object reuse, Decoder in, Field field) throws IOException {

        AdvancedCustomEncoding<?> encoding = null;

        if(field.getType() == ZonedDateTime.class){
            encoding = new ZonedDateTimeEncoder();
        }

//        encoding.getLogicalType().addToSchema(schema);

        return encoding.read(reuse, in, field);
    }

}
