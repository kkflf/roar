/*
 * MIT License
 *
 * Copyright (c) 2021 Kristian Kolding Foged-Ladefoged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
