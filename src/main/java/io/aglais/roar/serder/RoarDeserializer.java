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
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.NonRecordContainer;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.common.errors.SerializationException;
import org.codehaus.jackson.node.JsonNodeFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

public class RoarDeserializer extends KafkaAvroDeserializer {

    private final DecoderFactory decoderFactory = DecoderFactory.get();
    private final ReflectData reflectData = RoarReflectData.get();

    public RoarDeserializer() {
    }

    public RoarDeserializer(SchemaRegistryClient client) {
        this.schemaRegistry = client;
    }

    public RoarDeserializer(SchemaRegistryClient client, Map<String, ?> props) {
        super(client, props);
    }


    public Object deserialize(String s, byte[] bytes) {
        return this.deserialize(bytes);
    }

    public Object deserialize(String s, byte[] bytes, Schema readerSchema) {
        return this.deserialize(bytes, readerSchema);
    }

    protected Object deserialize(boolean includeSchemaAndVersion, String topic, Boolean isKey,
                                 byte[] payload, Schema readerSchema) throws SerializationException {
        // Even if the caller requests schema & version, if the payload is null we cannot include it.
        // The caller must handle this case.
        if (payload == null) {
            return null;
        }

        int id = -1;
        try {
            ByteBuffer buffer = getByteBuffer(payload);
            id = buffer.getInt();
            Schema schema = schemaRegistry.getById(id);
            int length = buffer.limit() - 1 - idSize;
            final Object result;
            if (schema.getType().equals(Schema.Type.BYTES)) {
                byte[] bytes = new byte[length];
                buffer.get(bytes, 0, length);
                result = bytes;
            } else {
                int start = buffer.position() + buffer.arrayOffset();
                DatumReader reader = getDatumReader(schema, readerSchema);
                Object
                        object =
                        reader.read(null, decoderFactory.binaryDecoder(buffer.array(), start, length, null));

                if (schema.getType().equals(Schema.Type.STRING)) {
                    object = object.toString(); // Utf8 -> String
                }
                result = object;
            }

            if (includeSchemaAndVersion) {
                // Annotate the schema with the version. Note that we only do this if the schema +
                // version are requested, i.e. in Kafka Connect converters. This is critical because that
                // code *will not* rely on exact schema equality. Regular deserializers *must not* include
                // this information because it would return schemas which are not equivalent.
                //
                // Note, however, that we also do not fill in the connect.version field. This allows the
                // Converter to let a version provided by a Kafka Connect source take priority over the
                // schema registry's ordering (which is implicit by auto-registration time rather than
                // explicit from the Connector).
                String subject = getSubjectName(topic, isKey, result);
                Integer version = schemaRegistry.getVersion(subject, schema);
                if (schema.getType() == Schema.Type.UNION) {
                    // Can't set additional properties on a union schema since it's just a list, so set it
                    // on the first non-null entry
                    for (Schema memberSchema : schema.getTypes()) {
                        if (memberSchema.getType() != Schema.Type.NULL) {
                            memberSchema.addProp(SCHEMA_REGISTRY_SCHEMA_VERSION_PROP,
                                    JsonNodeFactory.instance.numberNode(version));
                            break;
                        }
                    }
                } else {
                    schema.addProp(SCHEMA_REGISTRY_SCHEMA_VERSION_PROP,
                            JsonNodeFactory.instance.numberNode(version));
                }
                if (schema.getType().equals(Schema.Type.RECORD)) {
                    return result;
                } else {
                    return new NonRecordContainer(schema, result);
                }
            } else {
                return result;
            }
        } catch (IOException | RuntimeException e) {
            // avro deserialization may throw AvroRuntimeException, NullPointerException, etc
            throw new SerializationException("Error deserializing Avro message for id " + id, e);
        } catch (RestClientException e) {
            throw new SerializationException("Error retrieving Avro schema for id " + id, e);
        }
    }

    private ByteBuffer getByteBuffer(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        if (buffer.get() != MAGIC_BYTE) {
            throw new SerializationException("Unknown magic byte!");
        }
        return buffer;
    }

    private DatumReader getDatumReader(Schema writerSchema, Schema readerSchema) {
        return readerSchema == null ? reflectData.createDatumReader(writerSchema) : reflectData.createDatumReader(writerSchema, readerSchema);
    }
}
