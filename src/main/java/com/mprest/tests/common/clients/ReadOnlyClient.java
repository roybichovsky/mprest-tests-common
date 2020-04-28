package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.ReadOnlyRequest;

import java.util.concurrent.Future;

public interface ReadOnlyClient {
    Boolean setReadOnlyMode(ReadOnlyRequest request);
    Future<Boolean> setReadOnlyModeAsync(ReadOnlyRequest requestData);
}
