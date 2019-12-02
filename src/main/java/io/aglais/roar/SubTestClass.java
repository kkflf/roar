package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubTestClass {

    @RoarData(documentation = "This is a value")
    private int value;

}
