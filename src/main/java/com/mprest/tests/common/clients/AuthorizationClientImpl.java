package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AuthorizationClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.*;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class AuthorizationClientImpl extends BaseServiceClient implements AuthorizationClient {

    private final String ROLES_URL = baseUrl + "/roles";
    private final String USERS_URL = baseUrl + "/users";

    public AuthorizationClientImpl(SecuredServerConfiguration secured, AuthorizationClientConfiguration clientConfig) {
        super(AuthorizationClientImpl.class, secured, clientConfig);
    }

    @Override
    public RoleData createRole(RoleRequestData roleClient) {
        Future<RoleData> roleAsync = createRoleAsync(roleClient);
        return extractAsyncResult(roleAsync, "Failed to create role");
    }

    @Override
    public RoleData updateRole(RoleUpdateData roleClient) {
        Future<RoleData> roleAsync = updateRoleAsync(roleClient);
        return extractAsyncResult(roleAsync, "Failed to update role");
    }

    @Override
    public Optional<RoleData> getRole(long id) {
        Future<Optional<RoleData>> roleFuture = getRoleAsync(id);
        return extractOptionalAsyncResult(roleFuture, "Failed to get role");
    }

    @Override
    public Boolean deleteRole(long id) {
        Future<Boolean> booleanFuture = deleteRoleAsync(id);
        return extractAsyncResult(booleanFuture, "Failed to delete role");
    }

    @Override
    public Boolean assignRoleToUser(AssignRoleToUsersClient assignRoleToUsersClient) {
        Future<Boolean> booleanFuture = assignRoleToUsersAsync(assignRoleToUsersClient);
        return extractAsyncResult(booleanFuture, "Failed to assign role to user");
    }

    @Override
    public PermissionsClient getUserPermissions(String user) {
        Future<PermissionsClient> userPermissions = getUserPermissionsAsync(user);
        return extractAsyncResult(userPermissions, "Failed to get user permissions");
    }

    @Override
    public List<String> getUsers() {
        Future<List<String>> userPermissions = getUsersAsync();
        return extractAsyncResult(userPermissions, "Failed to get users");
    }

    @Override
    public Future<Optional<RoleData>> getRoleAsync(long id) {
        String url = ROLES_URL + "/{roleId}";
        log.info("posting to URL {}", url);
        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .pathParam("roleId", id)
                            .when()
                                    .get(url)
                            .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    RoleData roleClient = response.body().as(RoleData.class);
                    log.debug("Fetching role {} data", roleClient.getId());
                    return Optional.of(roleClient);
                }
        );
    }

    @Override
    public Future<RoleData> createRoleAsync(RoleRequestData roleClient) {
        log.info("posting to URL {}", ROLES_URL);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .contentType(ContentType.JSON)
                                    .body(roleClient)
                            .when()
                                    .post(ROLES_URL)
                            .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.CREATED.value())
                                    .extract()
                                    .response();
                    return response.body().as(RoleData.class);
                }
        );
    }

    @Override
    public Future<Boolean> assignRoleToUsersAsync(AssignRoleToUsersClient assignRoleToUsersClient) {
        String url = ROLES_URL + "/users/assign";
        log.info("posting to URL {}", url);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .contentType(ContentType.JSON)
                                    .body(assignRoleToUsersClient)
                            .when()
                                    .post(url)
                            .then()
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    return true;
                }
        );
    }

    @Override
    public Future<RoleData> updateRoleAsync(RoleUpdateData roleClient) {
        log.info("posting to URL {}", ROLES_URL);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .contentType(ContentType.JSON)
                                    .body(roleClient)
                           .when()
                                    .put(ROLES_URL)
                           .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    return response.body().as(RoleData.class);
                }
        );
    }

    @Override
    public Future<Boolean> deleteRoleAsync(long id) {
        String url = ROLES_URL + "/{roleId}";
        log.info("deleting role id by url {}", url);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .pathParam("roleId", id)
                                    .when()
                                    .delete(url)
                            .then()
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    return true;
                }
        );
    }

    @Override
    public Future<PermissionsClient> getUserPermissionsAsync(String user) {
        String url = USERS_URL + "/permissions/" + user;
        log.info("posting to URL {}", url);
        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .when()
                                    .get(url)
                                    .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    return response.body().as(PermissionsClient.class);
                }
        );
    }

    @Override
    public Future<List<String>> getUsersAsync() {
        log.info("posting to URL {}", USERS_URL);
        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .when()
                                    .get(USERS_URL)
                                    .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    return response.body().as(List.class);
                }
        );
    }

}
