package io.aglais.roar.entity;

import io.aglais.roar.annotations.api.RoarData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    @RoarData(documentation = "This is the event id")
    private String eventId;
}
