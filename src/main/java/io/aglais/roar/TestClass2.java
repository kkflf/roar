package io.aglais.roar;

import io.aglais.roar.entity.Metadata;
import io.aglais.roar.entity.RoarEvent;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@ToString(callSuper = true)
@Data
@Builder
public class TestClass2 extends RoarEvent {

    private SubTestClass subTestClass;

}
