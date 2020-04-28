package com.mprest.tests.common.data.feeders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeederNodeData {
    private String id;
    private String name;
    private String feederId;
    private String source;
    private String target;
    private String type;
    private int energized;
    private int switchState;
}
