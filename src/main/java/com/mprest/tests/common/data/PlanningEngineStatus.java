package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanningEngineStatus implements Serializable {
    public static final PlanningEngineStatus Unknown = PlanningEngineStatus.builder()
            .status(CalculationStatus.UNKNOWN)
            .triggeredBy(Collections.singleton(Trigger.UNKNOWN))
            .deferredTriggers(Collections.singleton(""))
            .build();

    private CalculationStatus status;
    private Set<Trigger> triggeredBy;
    private Set<String> deferredTriggers;
    private Instant timestamp;
    private Instant nextRunTimestamp;
    private Instant lastRunTimestamp;
    private String errorDescription;

    public enum CalculationStatus {
        CALCULATING,
        UPDATED,
        UPDATING_CACHE,
        UNKNOWN,
        FAILURE_PROCESSING_RESULTS,
        PLANNER_ERROR
    }

    public enum Trigger {
        DR, MANUAL, SCHEDULE, TOPOLOGY, STARTUP, USER_DEFINED, POLICY_CHANGE, ADAPTER_UP, ADAPTER_DOWN, UNKNOWN
    }
}
