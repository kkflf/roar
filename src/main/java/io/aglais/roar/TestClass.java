package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarData;
import io.aglais.roar.annotations.api.RoarDate;
import io.aglais.roar.annotations.api.RoarGdpr;
import io.aglais.roar.annotations.api.RoarSchema;
import io.aglais.roar.encoders.DateFactoryEncoder;
import io.aglais.roar.entity.Metadata;
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
    @RoarData(documentation = "This is a value for an integer - myIntegerValue")
    private int myIntegerValue;

    @RoarData(documentation = "This ia double value - myDoubleValue")
    @RoarGdpr(value = RoarGdpr.GdprType.PII)
    private double myDoubleValue;

    @RoarData(documentation = "This ia double value - subTestClass")
    private SubTestClass subTestClass;

    @RoarData(documentation = "This is a value - valueFieldField12")
    private int valueFieldField12;

    @RoarDate(timezone = "EET", documentation = "This is a value - date")
    @AvroEncode(using = DateFactoryEncoder.class)
    private ZonedDateTime date;

}
