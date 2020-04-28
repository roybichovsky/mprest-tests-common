package com.mprest.tests.common.data.RuleEngine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcedureGroup {

    private long id;
    private String name;
    private List<Procedure> procedures;
    private Boolean isActive;
    private Instant createTime = null;
    private Instant lastUpdateTime = null;
}
