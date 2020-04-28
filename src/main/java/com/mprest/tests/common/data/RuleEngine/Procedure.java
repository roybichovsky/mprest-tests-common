package com.mprest.tests.common.data.RuleEngine;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Procedure {

    private long id;
    private String name;
    private String procedureTypeId;
    private String data;
    private Boolean isActive;
}
