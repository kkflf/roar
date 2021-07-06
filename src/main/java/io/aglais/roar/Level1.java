/*
 * MIT License
 *
 * Copyright (c) 2021 Kristian Kolding Foged-Ladefoged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
