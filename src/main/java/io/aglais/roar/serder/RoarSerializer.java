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

package io.aglais.roar.serder;

import io.aglais.roar.reflection.RoarReflectData;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.common.errors.SerializationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Map;

public class RoarSerializer extends KafkaAvroSerializer {

    private final EncoderFactory encoderFactory = EncoderFactory.get();
    private final RoarReflectData reflectData = new RoarReflectData();

    public RoarSerializer() {
    }

    public RoarSerializer(SchemaRegistryClient client) {
        super(client);
    }

    public RoarSerializer(SchemaRegistryClient client, Map<String, ?> props) {
        super(client, props);
    }

    public void configure(Map<String, ?> configs, boolean isKey) {
        super.configure(configs, isKey);
    }


    @Override
    protected Schema getSchema(Object object) {
        try {
            return reflectData.getSchema(object.getClass());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new SerializationException("Error serializing Avro message", e);
        }
    }


    @Override
    protected byte[] serializeImpl(String subject, Object object) {

        if (object == null) {
            return null;
        }

        String restClientErrorMsg = "";

        Schema schema = getSchema(object);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            int id;
            if (autoRegisterSchema) {
                restClientErrorMsg = "Error registering Avro schema: ";
                id = schemaRegistry.register(subject, schema);
            } else {
                restClientErrorMsg = "Error retrieving Avro schema: ";
                id = schemaRegistry.getId(subject, schema);
            }

            out.write(MAGIC_BYTE);
            out.write(ByteBuffer.allocate(idSize).putInt(id).array());

            BinaryEncoder encoder = encoderFactory.directBinaryEncoder(out, null);
            DatumWriter<Object> writer = reflectData.createDatumWriter(schema);

            writer.write(object, encoder);
            encoder.flush();

            return out.toByteArray();
        } catch (IOException | RuntimeException e) {
            throw new SerializationException("Error serializing Avro message", e);
        } catch (RestClientException e) {
            throw new SerializationException(restClientErrorMsg + schema, e);
        }
    }


}
