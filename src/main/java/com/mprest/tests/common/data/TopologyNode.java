package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopologyNode implements Comparable<TopologyNode> {
    private String id;
    private String name;
    private String type;
    private Map<String, Object> propertiesMap;
    private List<TopologyNode> children;

    public int compareTo(TopologyNode topologyNode) {
        if (topologyNode == null || this.getName() == null ||
                topologyNode.getName() == null) {
            return 0;
        }
        return this.getName().compareTo(topologyNode.getName());
    }
}
