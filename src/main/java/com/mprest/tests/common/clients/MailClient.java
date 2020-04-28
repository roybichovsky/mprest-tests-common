package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.MailMessage;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

public interface MailClient {
    Optional<MailMessage[]> getMailMessages(String folderName, Instant startDate, Instant endDate, int recordsLimit);
    Future<Optional<MailMessage[]>> getMailMessagesAsync(String folderName, Instant startDate, Instant endDate, int recordsLimit);
}
