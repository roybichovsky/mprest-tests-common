package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

public interface AuthorizationClient {

    RoleData createRole(RoleRequestData roleClient);
    Future<RoleData> createRoleAsync(RoleRequestData roleClient);

    RoleData updateRole(RoleUpdateData roleClient);
    Future<RoleData> updateRoleAsync(RoleUpdateData roleClient);

    Optional<RoleData> getRole(long id);
    Future<Optional<RoleData>> getRoleAsync(long id);

    Boolean deleteRole(long id);
    Future<Boolean> deleteRoleAsync(long id);

    Boolean assignRoleToUser(AssignRoleToUsersClient assignRoleToUsersClient);
    Future<Boolean> assignRoleToUsersAsync(AssignRoleToUsersClient assignRoleToUsersClient);

    PermissionsClient getUserPermissions(String user);
    Future<PermissionsClient> getUserPermissionsAsync(String user);

    List<String> getUsers();
    Future getUsersAsync();

}
