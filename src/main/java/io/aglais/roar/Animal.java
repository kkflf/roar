package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarGdpr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animal {

    @RoarGdpr(documentation = "This is the name of the pet", value = RoarGdpr.GdprType.PII, key = "member")
    private String name;

}
