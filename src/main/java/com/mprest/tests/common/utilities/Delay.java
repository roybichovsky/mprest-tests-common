package com.mprest.tests.common.utilities;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static org.junit.Assert.fail;

@Slf4j
public class Delay {
    public static void waitFor(Duration duration) {
        try {
            log.debug("Wait '{}' milliseconds", duration.toMillis());
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException ex) {
            String error = "Interrupted while waiting";
            log.error(error, ex);
            fail(error);
        }
    }
}
