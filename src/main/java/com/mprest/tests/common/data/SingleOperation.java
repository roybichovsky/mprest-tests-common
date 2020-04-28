package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleOperation{
    String resourceId;
    String resourceType;
    String value;
    long durationMin;
    String operation;
    String plannedFromGrid;
    String startTime;
}