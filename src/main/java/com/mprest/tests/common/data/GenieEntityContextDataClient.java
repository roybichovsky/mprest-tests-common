package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class GenieEntityContextDataClient {
    private String entityId;
    private String tenant;
    private String tags;
    private String data;
    private String context;
    private String source;

    @JsonIgnore
    private Instant lastUpdated;
}
