package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleAlert {
    private String alertId;
    private String assetId;
    private String topologyNodeId;
    private Instant startTime;
    private Instant calcTime;
    private int durationMin;
    private String alertType;
    private boolean resolved;
    private String severity;
    private String description;
}
