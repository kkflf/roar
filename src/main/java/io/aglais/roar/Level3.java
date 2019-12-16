package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarGdpr;

public class Level3 {

    @RoarGdpr(value = RoarGdpr.GdprType.PII, key = "1")
    private String level3ValueA = "level3ValueA-val";

    @RoarGdpr(value = RoarGdpr.GdprType.PII, key = "2")
    private String level3ValueB = "level3ValueB-val";

    @RoarGdpr(value = RoarGdpr.GdprType.KEY, key = "1")
    private int level3IdC = -4;

}
