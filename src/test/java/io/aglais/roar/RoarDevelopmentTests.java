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
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Properties;

public class RoarDevelopmentTests {

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
    public void asd() throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        System.out.println(ZonedDateTime.now());

        Metadata metadata = Metadata
                .builder()
                .eventId("event-id-123")
                .build();

        TestClass2 testClass2 = TestClass2.builder()
                .testClass2ValueValue("asd")
                .build();

        SubTestClass subTestClass = SubTestClass
                .builder()
                .valueFieldField(12)
                .testClass2Value(testClass2)
                .build();

        TestClass testClass = TestClass
                .builder()
                .date(ZonedDateTime.now())
                .myDoubleValue(2.5)
                .myIntegerValue(11)
                .subTestClass(subTestClass)
                .build();

        testClass.setMetadata(metadata);

        System.out.println(testClass);

        RoarReflectData roarReflectData = new RoarReflectData();
        Schema schema = roarReflectData.getSchema(TestClass.class);
        System.out.println(schema);

        byte[] serializedData = roarAvroSerializer.serialize("topic", testClass);

        System.out.println("Serialized!");

        TestClass actual = (TestClass) roarAvroDeserializer.deserialize("topic", serializedData);

        System.out.println(actual);


    }
}
