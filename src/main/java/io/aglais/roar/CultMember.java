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
public class CultMember extends RoarEvent {

    @RoarGdpr(documentation = "This is the member id", value = RoarGdpr.GdprType.KEY, key = "member")
    private int memberId;

    @RoarGdpr(documentation = "This is the full name of the customer", value = RoarGdpr.GdprType.PII, key = "member")
    private String fullName;

    @RoarGdpr(documentation = "This is the recruiter id", value = RoarGdpr.GdprType.KEY, key = "recruiter")
    private int recruiterId;

    @RoarGdpr(documentation = "This is the full name of the recruiter", value = RoarGdpr.GdprType.PII, key = "recruiter")
    private String recruiterFullName;

    @RoarDate(timezone = "EET", documentation = "This is when the member joined the cult")
    @AvroEncode(using = DateFactoryEncoder.class)
    private ZonedDateTime joined;

    @RoarData(documentation = "This is the members pet")
    private Animal pet;

}
