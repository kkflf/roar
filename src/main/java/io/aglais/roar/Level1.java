package io.aglais.roar;

import io.aglais.roar.annotations.api.RoarGdpr;
import io.aglais.roar.annotations.api.RoarSchema;
import io.aglais.roar.entity.RoarEvent;

@RoarSchema
public class Level1 extends RoarEvent {

    @RoarGdpr(value = RoarGdpr.GdprType.KEY, key = "1")
    private int level1IdA = -1;

    @RoarGdpr(value = RoarGdpr.GdprType.KEY, key = "1")
    private String level1ValueB = "level1ValueB-val";

    @RoarGdpr(value = RoarGdpr.GdprType.KEY, key = "2")
    private int level2IdA = -2;

    @RoarGdpr(value = RoarGdpr.GdprType.PII, key = "2")
    private String level2ValueB = "level2ValueB-val";

    private Level2 level2 = new Level2();

}
