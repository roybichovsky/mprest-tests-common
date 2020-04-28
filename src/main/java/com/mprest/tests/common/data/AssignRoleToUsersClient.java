package com.mprest.tests.common.data;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class AssignRoleToUsersClient implements Serializable {
    private Long roleId;
    private List<String> users;
}
