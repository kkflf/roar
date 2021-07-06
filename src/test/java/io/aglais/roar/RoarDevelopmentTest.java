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

package io.aglais.roar;

import io.aglais.roar.config.RoarConfig;
import io.aglais.roar.entity.Metadata;
import io.aglais.roar.reflection.RoarReflectData;
import io.aglais.roar.serder.RoarDeserializer;
import io.aglais.roar.serder.RoarSerializer;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.avro.Schema;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Properties;

public class RoarDevelopmentTest {

    private static SchemaRegistryClient schemaRegistry;
    private static RoarSerializer roarAvroSerializer;
    private static RoarDeserializer roarAvroDeserializer;

    @BeforeClass
    public static void setupClass() throws Exception {
        Properties defaultConfig = new Properties();
        defaultConfig.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "bogus");
        defaultConfig.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        schemaRegistry = new MockSchemaRegistryClient();
        roarAvroSerializer = new RoarSerializer(schemaRegistry, new HashMap(defaultConfig));
        roarAvroDeserializer = new RoarDeserializer(schemaRegistry, new HashMap(defaultConfig));

        RoarConfig roarConfig = RoarConfig.getInstance();

        roarConfig.defaultConfig();
    }

    @Test
    public void asd() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        System.out.println(ZonedDateTime.now());

        Metadata metadata = Metadata
                .builder()
                .eventId("event-id-123")
                .build();


        Animal pet = Animal
                .builder()
                .name("Brian")
                .build();

        CultMember customer = CultMember
                .builder()
                .joined(ZonedDateTime.now())
                .fullName("Donald")
                .memberId(2)
                .recruiterId(1)
                .recruiterFullName("Peter")
                .pet(pet)
                .build();

        customer.setMetadata(metadata);

        System.out.println(customer);

        RoarReflectData roarReflectData = new RoarReflectData();
        Schema schema = roarReflectData.getSchema(CultMember.class);
        System.out.println(schema);

        byte[] serializedData = roarAvroSerializer.serialize("topic", customer);

        System.out.println("Serialized!");

        CultMember actual = (CultMember) roarAvroDeserializer.deserialize("topic", serializedData);

        System.out.println(actual);

        Assert.assertTrue(true);

    }

    @Test
    public void abas() throws IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        System.out.println(ZonedDateTime.now());

        Metadata metadata = Metadata
                .builder()
                .eventId("event-id-123")
                .build();


        Level1 level1 = new Level1();

        level1.setMetadata(metadata);

        System.out.println(level1);

        RoarReflectData roarReflectData = new RoarReflectData();
        Schema schema = roarReflectData.getSchema(CultMember.class);
        System.out.println(schema);

        byte[] serializedData = roarAvroSerializer.serialize("topic", level1);

        System.out.println("Serialized!");

        Level1 actual = (Level1) roarAvroDeserializer.deserialize("topic", serializedData);

        System.out.println(actual);

        Assert.assertEquals(actual, level1);

    }
}
