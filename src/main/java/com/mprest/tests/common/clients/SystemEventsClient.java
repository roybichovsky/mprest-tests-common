package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.SystemEvent.SystemEvent;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

public interface SystemEventsClient {
    Optional<SystemEvent[]> getSystemEventsBetweenDates(Instant from, Instant to);

    Future<Optional<SystemEvent[]>> getSystemEventsBetweenDatesAsync(Instant from, Instant to);
}