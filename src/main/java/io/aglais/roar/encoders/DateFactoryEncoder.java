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
