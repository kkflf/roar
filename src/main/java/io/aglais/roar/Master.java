package io.aglais.roar;

import io.aglais.roar.config.RoarConfig;
import io.aglais.roar.encoders.AdvancedCustomEncoding;
import io.aglais.roar.reflection.RoarReflectData;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.Encoder;

import java.io.IOException;

public class Master {

    public static void main(String[] args) throws Exception {

        RoarConfig roarConfig = RoarConfig.getInstance();
        roarConfig.defaultConfig();


        RoarReflectData roarReflectData = new RoarReflectData();

//        RoarReflectData roarReflectData = new RoarReflectData();
//        Schema schema = roarReflectData.getSchema(TestClass.class);
//
//        System.out.println(schema);
//
//        Schema schema1 = roarReflectData.getSchema(TestClass2.class);
//        System.out.println(schema1);

    }

//    @Override
//    protected void read(Object object, org.apache.avro.io.Decoder in) throws java.io.IOException {
//        try {
//            if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {
//                field.set(object, ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).read(in), field);
//            } else {
//                field.set(object, encoding.read(in));
//            }
//        } catch (java.lang.IllegalAccessException e) {
//            throw new org.apache.avro.AvroRuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void write(Object object, org.apache.avro.io.Encoder out) throws java.io.IOException {
//        try {
//            if(encoding instanceof io.aglais.roar.encoders.AdvancedCustomEncoding) {
//                ((io.aglais.roar.encoders.AdvancedCustomEncoding) encoding).write(field.get(object), out);
//            } else {
//                encoding.write(field.get(object), out);
//            }
//        } catch (java.lang.IllegalAccessException e) {
//            throw new org.apache.avro.AvroRuntimeException(e);
//        }
//    }
}
