package com.mprest.tests.common.time;

import io.vavr.control.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.TimeZone;

@Service
@Slf4j
public class TimeManagerImpl implements TimeManager {

    private static final String NULL = "NULL";

    private Option<Instant> timeOverride = Option.none();
    private final TimeZone timeZone;

    @Autowired
    public TimeManagerImpl(TimeManagerConfiguration timeManagerConfiguration) {
        getConfiguredTime(timeManagerConfiguration.getUtcnow())
                .ifPresent(this::setTimeOverride);

        timeZone = getConfiguredTimezone(Option.of(timeManagerConfiguration.getTimezone()).getOrElse(NULL));
    }

    private Optional<Instant> getConfiguredTime(String utcNow) {
        if (StringUtils.isEmpty(utcNow) || NULL.equals(utcNow.toUpperCase())) return Optional.empty();
        return parseInstant(utcNow);
    }

    private Optional<Instant> parseInstant(String instantString) {
        return Optional.ofNullable(tryParseInstant(instantString));
    }

    private static Instant tryParseInstant(String instantString) {
        try {
            return Instant.parse(instantString);
        } catch (Exception ex) {
            log.error("Parsing '{}' failed: '{}'", instantString, ex);
            return null;
        }
    }

    private static TimeZone getConfiguredTimezone(String timezone) {
        if (StringUtils.isEmpty(timezone) || NULL.equals(timezone.toUpperCase())) return TimeZone.getDefault();

        return Option.of(timezone)
                .map(TimeZone::getTimeZone)
                .getOrElse(() -> {
                    log.warn("No time zone was configured, using the default time zone");
                    return TimeZone.getDefault();
                });
    }

    @Override
    public TimeStruct Now() {
        return TimeStruct.FromUtcTime(timeOverride.getOrElse(Instant::now), timeZone);
    }

    @Override
    public boolean isTimeManipulated() {
        return timeOverride.isDefined();
    }

    private void setTimeOverride(Instant newTimeOverride) {
        timeOverride = Option.of(newTimeOverride)
                .peek(time -> log.info("New time override was set to '{}'", time))
                .onEmpty(() -> timeOverride
                        .peek(time -> log.info("Time override was removed. Previous value was '{}'", time)));
    }
}
