package io.aglais.roar.encoders;

import io.aglais.roar.annotations.api.RoarData;
import io.aglais.roar.annotations.api.RoarDate;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public class DateEncoder extends AdvancedCustomEncoding<Date> {

    {
        schema = Schema.create(Schema.Type.LONG);
        LogicalType logicalType = new LogicalType("myLogicalType");
        logicalType.addToSchema(schema);
    }

    @Override
    public void write(Object datum, Encoder out, Field field) throws IOException {

        RoarDate roarDate = field.getDeclaredAnnotation(RoarDate.class);

        System.out.println("WRITE:" + roarDate.timezone());

        System.out.println(field.getAnnotations());
        out.writeLong(((Date) datum).getTime());
    }

    @Override
    public Date read(Object reuse, Decoder in, Field field) throws IOException {

        RoarDate roarDate = field.getDeclaredAnnotation(RoarDate.class);

        System.out.println("WRITE:" + roarDate.timezone());

        if (reuse != null && reuse instanceof Date) {
            ((Date) reuse).setTime(in.readLong());
            return (Date) reuse;
        } else return new Date(in.readLong());
    }

}
