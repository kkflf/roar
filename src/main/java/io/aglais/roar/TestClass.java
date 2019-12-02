package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarData;
import io.aglais.roar.annotations.api.RoarDate;
import io.aglais.roar.annotations.api.RoarGdpr;
import io.aglais.roar.annotations.api.RoarSchema;
import io.aglais.roar.encoders.DateFactoryEncoder;
import io.aglais.roar.entity.RoarEvent;
import lombok.*;
import org.apache.avro.reflect.AvroEncode;

import java.time.ZonedDateTime;

@ToString(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RoarSchema
public class TestClass extends RoarEvent {

    @RoarGdpr(value = RoarGdpr.GdprType.KEY)
    @RoarData(documentation = "This is a value for an integer")
    private int myIntegerValue;

    @RoarData(documentation = "This ia double value")
    @RoarGdpr(value = RoarGdpr.GdprType.PII)
    private double myDoubleValue;

    private SubTestClass subTestClass;

    @RoarDate(timezone = "EET")
    @AvroEncode(using = DateFactoryEncoder.class)
    private ZonedDateTime date;

}
