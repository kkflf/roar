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

package io.aglais.roar.config;

import javassist.*;
import org.apache.avro.Schema;

import java.lang.reflect.Field;

public class RoarConfig {

    private static final RoarConfig INSTANCE = new RoarConfig();
    private AvroModifier avroModifier;

    private RoarConfig() {
        this.avroModifier = new AvroModifier();

    }

    public static RoarConfig getInstance() {
        return INSTANCE;
    }

    public void defaultConfig() throws Exception {
        avroModifier.removeReservedKey(Schema.class, "SCHEMA_RESERVED", "doc");
        avroModifier.removeReservedKey(Schema.class, "FIELD_RESERVED", "doc");
//        injectMethodToFieldAccessReflect();
        injectMethodToFieldAccessUnsafe();
    }

    public void test1() throws Exception {
        test2();
    }

    public void test2() throws Exception {
        Exception exception = new Exception("This is the first exception!");
        throw new Exception("This is the message!", exception);
    }

    public void injectMethodToFieldAccessReflect() throws Exception {

        ClassPool classPool = ClassPool.getDefault();

        CtClass ctClass = classPool.get("org.apache.avro.reflect.FieldAccessReflect$ReflectionBasesAccessorCustomEncoded");

        CtMethod readMethod = ctClass.getDeclaredMethod("read");
        CtMethod writeMethod = ctClass.getDeclaredMethod("write");

        ctClass.removeMethod(readMethod);
        ctClass.removeMethod(writeMethod);

        CtMethod newReadMethod = CtNewMethod.make(getReadMethodFieldAccessReflect(), ctClass);
        ctClass.addMethod(newReadMethod);

        CtMethod newWriteMethod = CtNewMethod.make(getWriteMethodFieldAccessReflect(), ctClass);
        ctClass.addMethod(newWriteMethod);

        ctClass.toClass();

        System.out.println(readMethod);
        System.out.println(writeMethod);
    }

    private String getReadMethodFieldAccessReflect() {
        return "    protected void read(Object object, org.apache.avro.io.Decoder in) throws java.io.IOException {\n" +
                "        try {\n" +
                "            if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {\n" +
                "                field.set(object, ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).read(in, field));\n" +
                "            } else {\n" +
                "                field.set(object, encoding.read(in));\n" +
                "            }\n" +
                "        } catch (java.lang.IllegalAccessException e) {\n" +
                "            throw new org.apache.avro.AvroRuntimeException(e);\n" +
                "        }\n" +
                "    }";
    }

    private String getWriteMethodFieldAccessReflect() {
        return "protected void write(Object object, org.apache.avro.io.Encoder out) throws java.io.IOException {\n" +
                "        try {\n" +
                "            if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {\n" +
                "                ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).write(field.get(object), out);\n" +
                "            } else {\n" +
                "                encoding.write(field.get(object), out);\n" +
                "            }\n" +
                "        } catch (java.lang.IllegalAccessException e) {\n" +
                "            throw new org.apache.avro.AvroRuntimeException(e);\n" +
                "        }\n" +
                "    }";
    }

    public void injectMethodToFieldAccessUnsafe() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass1 = classPool.get("org.apache.avro.reflect.FieldAccessUnsafe");

        CtField oldUNSAFE = ctClass1.getField("UNSAFE");
        CtField newUNSAFE = CtField.make("public static final sun.misc.Unsafe UNSAFE;", ctClass1);

        ctClass1.removeField(oldUNSAFE);
        ctClass1.addField(newUNSAFE);

        CtClass ctClass = classPool.get("org.apache.avro.reflect.FieldAccessUnsafe$UnsafeCustomEncodedField");

        CtMethod readMethod = ctClass.getDeclaredMethod("read");
        CtMethod writeMethod = ctClass.getDeclaredMethod("write");

        ctClass.removeMethod(readMethod);
        ctClass.removeMethod(writeMethod);

        CtMethod newReadMethod = CtNewMethod.make(getReadMethodFieldAccessUnsafe(), ctClass);
        ctClass.addMethod(newReadMethod);

        CtMethod newWriteMethod = CtNewMethod.make(getWriteMethodFieldAccessUnsafe(), ctClass);
        ctClass.addMethod(newWriteMethod);

        ctClass1.toClass();
        ctClass.toClass();


        System.out.println(readMethod);
        System.out.println(writeMethod);

        Class clazz = Class.forName("org.apache.avro.reflect.FieldAccessUnsafe");
        Field unsafeField = clazz.getDeclaredField("UNSAFE");
        unsafeField.setAccessible(true);
    }



    private String getReadMethodFieldAccessUnsafe() {
        return "    protected void read(Object object, org.apache.avro.io.Decoder in) throws java.io.IOException {\n" +
                "       System.out.println(\"Hey read!!!\");\n" +
                "       if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {\n" +
                "            org.apache.avro.reflect.FieldAccessUnsafe.UNSAFE.putObject(object, offset, ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).read(in, field));\n" +
                "       } else {\n" +
                "            org.apache.avro.reflect.FieldAccessUnsafe.UNSAFE.putObject(object, offset, encoding.read(in));\n" +
                "       }\n" +
                "    }";
    }

    private String getWriteMethodFieldAccessUnsafe() {
        return "protected void write(Object object, org.apache.avro.io.Encoder out) throws java.io.IOException {\n" +
                "       System.out.println(\"Hey write!!!\");\n" +
                "        if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {\n" +
                "           ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).write(org.apache.avro.reflect.FieldAccessUnsafe.UNSAFE.getObject(object, offset), out, field);\n" +
                "        } else {\n" +
                "           encoding.write(org.apache.avro.reflect.FieldAccessUnsafe.UNSAFE.getObject(object, offset), out);\n" +
                "        }\n" +
                "    }";
    }


//    private String getReadMethodFieldAccessUnsafe() {
//        return "    protected void read(Object object, org.apache.avro.io.Decoder in) throws java.io.IOException {\n" +
//                "       System.out.println($$UNSAFE);\n" +
//                "    }";
//    }
//
//    private String getWriteMethodFieldAccessUnsafe() {
//        return "protected void write(Object object, org.apache.avro.io.Encoder out) throws java.io.IOException {\n" +
//                "        System.out.println(\"asda231\");\n" +
//                "    }";
//    }


}
