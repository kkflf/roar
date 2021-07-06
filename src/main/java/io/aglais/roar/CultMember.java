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
