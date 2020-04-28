package com.mprest.tests.common.data.RuleEngine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleData {
    private String id;
    private String name;
    private String description;
    private String ruleEntity;
    private String ruleEntityCategory;
    private String ruleData;
    private Boolean isActive;
    private String ruleTypeId;
    private ProcedureGroup procedureGroup;
    private ProcedureGroup cancellationProcedureGroup;
    private Instant createTime = null;
    private Instant lastUpdateTime = null;
}
