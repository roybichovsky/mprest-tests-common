package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleData implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Instant createdDate;
    private Instant updatedDate;
    private List<String> actions;
    private List<String> users;
}
