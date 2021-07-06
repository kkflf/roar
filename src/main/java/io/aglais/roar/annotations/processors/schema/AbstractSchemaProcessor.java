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

package io.aglais.roar.annotations.processors.schema;

import org.apache.avro.Schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractSchemaProcessor implements SchemaProcessor {

    private static final List<String> PRIMITIVE_TYPES = new ArrayList<>();

    protected Schema schema;
    protected Class<?> clazz;
    protected Map<String, Object> configProperties;

    public void initialize(Schema schema, Class<?> clazz, Map<String, Object> configProperties){
        this.schema = schema;
        this.clazz = clazz;
        this.configProperties = configProperties;
    }

    static {
        PRIMITIVE_TYPES.add("null");
        PRIMITIVE_TYPES.add("boolean");
        PRIMITIVE_TYPES.add("int");
        PRIMITIVE_TYPES.add("long");
        PRIMITIVE_TYPES.add("float");
        PRIMITIVE_TYPES.add("double");
        PRIMITIVE_TYPES.add("bytes");
        PRIMITIVE_TYPES.add("string");
    }

    protected boolean isComplexType(String typeName) {
        return !PRIMITIVE_TYPES.contains(typeName);
    }

    protected List<String> getPrimitiveTypes() {
        return PRIMITIVE_TYPES;
    }

}
