package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.UserConstraintsUI;

import java.util.Optional;
import java.util.concurrent.Future;

public interface UserConstraintsClient {

    Optional<UserConstraintsUI> getUserConstraints(String resourceId);

    Boolean saveUserConstraints(UserConstraintsUI userConstraintsUI);

    Future<Optional<UserConstraintsUI>> getUserConstraintsAsync(String resourceId);

    Future<Boolean> saveUserConstraintsAsync(UserConstraintsUI userConstraintsUI);
}
