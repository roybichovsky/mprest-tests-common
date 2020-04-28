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
public class UserConstraintsUI implements Timeable {
    private String approvedBy;
    private String assetId;
    private Boolean enabled;
    private Instant fromUTC;
    private String scope;
    private Instant toUTC;
    private String unit;
    private Double value;

    @Override
    public Instant getTime() {
        return fromUTC;
    }
}