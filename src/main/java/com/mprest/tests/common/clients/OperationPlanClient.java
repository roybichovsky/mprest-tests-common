package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.SingleOperation;
import com.mprest.tests.common.data.SingleOperationPlan;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public interface OperationPlanClient {

    SingleOperationPlan[] getOperationPlan() throws InterruptedException, ExecutionException, TimeoutException;

    Future<SingleOperationPlan[]> getOperationPlanAsync();
}
