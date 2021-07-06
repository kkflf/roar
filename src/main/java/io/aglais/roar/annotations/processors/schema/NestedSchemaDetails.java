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

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.avro.Schema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@ToString(exclude = "parent")
public class NestedSchemaDetails {
    private NestedSchemaDetails parent;
    private final List<NestedSchemaDetails> children = new ArrayList<>();
    private Class<?> clazz;
    private Schema schema;
    private Field field;
    private boolean isPrimitiveClass;


    public void addChild(NestedSchemaDetails child){
        children.add(child);
    }

    public List<String> getAllFieldNamesOrdered(){
        List<String> allFieldNamesOrdered;

        if(parent == null){
            allFieldNamesOrdered = new ArrayList<>();
        } else {
            allFieldNamesOrdered = parent.getAllFieldNamesOrdered();
            allFieldNamesOrdered.add(field.getName());
        }

        return allFieldNamesOrdered;
    }
}
