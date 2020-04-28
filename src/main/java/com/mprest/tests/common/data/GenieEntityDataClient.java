package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenieEntityDataClient {
    private String entityId;
    private String tenantId;
    private String type;
    private String tags;
    private String data;

    @JsonIgnore
    private Instant lastUpdated;
    private List<GenieEntityContextDataClient> genieEntityContextPOJOObjects;
}