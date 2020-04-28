package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingleOperationPlan {
    private String timestamp;
    private List<SingleOperation> operations;
}

