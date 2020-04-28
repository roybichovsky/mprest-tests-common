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
public class DemandModel implements Timeable {
    private String id;
    private double load;
    private String units;
    private Instant fromTime;
    private Instant toTime;
    private String source;

    @Override
    public Instant getTime() {
        return fromTime;
    }
}
