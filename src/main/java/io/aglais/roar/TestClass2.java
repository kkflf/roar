package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarData;
import io.aglais.roar.entity.Metadata;
import io.aglais.roar.entity.RoarEvent;
import lombok.*;

@ToString(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestClass2 {

//    private SubTestClass subTestClass;



    @RoarData(documentation = "This is a value")
    String testClass2ValueValue;

}
