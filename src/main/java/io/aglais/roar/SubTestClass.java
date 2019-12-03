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

    @RoarData(documentation = "This is a value - valueFieldField")
    private int valueFieldField;

    @RoarData(documentation = "This is a value - testClass2Value")
    TestClass2 testClass2Value;

}
