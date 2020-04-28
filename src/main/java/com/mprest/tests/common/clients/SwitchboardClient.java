package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.SwitchingEvent;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

public interface SwitchboardClient {
    default List<SwitchingEvent> getAllStates(String match) {
        return getAllStates(Collections.emptyList(), match);
    }

    default List<SwitchingEvent> getAllStates(List<String> switches) {
        return getAllStates(switches, null);
    }

    default List<SwitchingEvent> getAllStates() {
        return getAllStates(Collections.emptyList(), null);
    }

    List<SwitchingEvent> getAllStates(List<String> switches, String match);
    Future<List<SwitchingEvent>> getAllStatesAsync(List<String> switches, String match);

    Boolean save(List<SwitchingEvent> events);
    Future<Boolean> saveAsync(List<SwitchingEvent> events);

    List<SwitchingEvent> getAllStatesSnapshot(List<String> switches, Long recordId, Instant time);
    Future<List<SwitchingEvent>> getAllStatesSnapshotAsync(List<String> switches, Long recordId, Instant time);
}
