package io.aglais.roar.entity;

import io.aglais.roar.annotations.api.RoarData;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public abstract class RoarEvent {
    @RoarData(documentation = "This is doc for metadata")
    private Metadata metadata;
}
