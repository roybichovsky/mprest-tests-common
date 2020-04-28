package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.DermsPolicy;

import java.util.concurrent.Future;

public interface DermsPolicyClient {

    DermsPolicy getPolicy();
    Future<DermsPolicy> getPolicyAsync();

    boolean setPolicy(DermsPolicy policy);
    Future<Boolean> setPolicyAsync(DermsPolicy policy);
}
