package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.DemandModel;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

public interface DemandResponseClient {
    HttpStatus saveDemandResponse(DemandModel demandModel);
    Boolean deleteDemandResponse(String demandResponseId);
    Optional<DemandModel[]> getDemandResponseBetweenDates(Instant fromDate, Instant toDate);

    Future<HttpStatus> saveDemandResponseAsync(DemandModel demandModel);
    Future<Boolean> deleteDemandResponseAsync(String demandResponseId);
    Future<Optional<DemandModel[]>> getDemandResponseBetweenDatesAsync(Instant fromDate, Instant toDate);
}
