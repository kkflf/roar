package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarGdpr;

public class Level2 {

    @RoarGdpr(value = RoarGdpr.GdprType.PII, key = "1")
    private String level2ValueA = "level2ValueA-val";

    @RoarGdpr(value = RoarGdpr.GdprType.PII, key = "2")
    private String level2ValueB = "level2ValueB-val";

    @RoarGdpr(value = RoarGdpr.GdprType.KEY, key = "2")
    private int level2IdC = -3;

    private Level3 level3 = new Level3();

}
