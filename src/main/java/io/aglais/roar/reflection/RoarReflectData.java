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

package io.aglais.roar.reflection;

import io.aglais.roar.annotations.api.RoarSchema;
import io.aglais.roar.annotations.processors.schema.SchemaProcessor;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class RoarReflectData extends ReflectData {


    private static final Map<Class<?>, Schema> CACHED_SCHEMAS = new HashMap<>();


    public synchronized Schema getSchema(Class<?> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        if (CACHED_SCHEMAS.containsKey(clazz)) {
            return CACHED_SCHEMAS.get(clazz);
        }

        Schema schema = ReflectData.get().getSchema(clazz);

        RoarSchema roarSchema = clazz.getAnnotation(RoarSchema.class);

        SchemaProcessor schemaProcessor = roarSchema.processor().getConstructor().newInstance();

        schemaProcessor.initialize(schema, clazz, getConfigProperties());

        schemaProcessor.postProcessor();

        return schema;
    }

    private Map<String, Object> getConfigProperties() {

        Map<String, Object> map = new HashMap<>();

        map.put("field-delimiter", "-");

        return map;
    }

}
