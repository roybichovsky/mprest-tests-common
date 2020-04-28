package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

public interface AuthorizationDemandResponseClient {

    HttpStatus saveDemandResponse(DemandModel demandModel);
    Future<HttpStatus> saveDemandResponseAsync(DemandModel demandModel);

    HttpStatus validateGetForbidden(Instant fromDate, Instant toDate);
    Future<HttpStatus> validateGetForbiddenAsync(Instant fromDate, Instant toDate, HttpStatus expectedHttpStatus);

    Optional<DemandModel[]> validateGetSuccess(Instant fromDate, Instant toDate);
    Future<Optional<DemandModel[]>> validateGetSuccessAsync(Instant fromDate, Instant toDate);

}
