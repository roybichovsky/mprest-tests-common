package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.KeyInfo;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public interface TopologyDerAssociationClient {
    Optional<Set<KeyInfo>> getDerAssociation(String assetId);

    Future<Optional<Set<KeyInfo>>> getDerAssociationAsync(String assetId);
}
