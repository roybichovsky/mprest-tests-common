package com.mprest.tests.common.time;

import lombok.Getter;

import java.time.*;
import java.util.TimeZone;

@Getter
public class TimeStruct {
    public static final int MILI_PER_SECOND = 1000;
    public static final int SECOND_PER_HOUR = 3600;

    private Instant utcInstant;
    private Double timeOffsetInHours;
    private ZoneOffset zoneOffset;
    private ZonedDateTime zonedDateTime;
    private ZoneId zoneId;
    private LocalDateTime localDateTime;
    private TimeZone timeZone;

    private TimeStruct(Instant utcDateTime, TimeZone overrideTimezone) {
        utcInstant = utcDateTime;
        int offsetForDate = overrideTimezone.getOffset(utcDateTime.toEpochMilli());
        this.timeOffsetInHours = (double) offsetForDate / MILI_PER_SECOND / SECOND_PER_HOUR;
        zoneOffset = ZoneOffset.ofTotalSeconds((int) (offsetForDate / MILI_PER_SECOND));
        zoneId = ZoneId.of(zoneOffset.getId());
        zonedDateTime = utcDateTime.atZone(zoneId);
        localDateTime = LocalDateTime.ofInstant(utcInstant, zoneId);
        timeZone = overrideTimezone;
    }

    public static TimeStruct FromUtcTime(Instant utcTime, TimeZone overrideTimezone) {
        return new TimeStruct(utcTime, overrideTimezone);
    }

    public static TimeStruct FromUtcTime(Instant utcTime) {
        OffsetDateTime odt = OffsetDateTime.now();
        TimeZone overrideTimezone = TimeZone.getDefault();
        return FromUtcTime(utcTime, overrideTimezone);
    }

    public static TimeStruct FromLocalTime(LocalDateTime localDateTime) {
        TimeZone overrideTimezone = TimeZone.getDefault();
        return FromLocalTime(localDateTime, overrideTimezone);
    }

    public static TimeStruct FromLocalTime(LocalDateTime localDateTime, TimeZone overrideTimezone) {
        return FromZonedDateTime(localDateTime.atZone(overrideTimezone.toZoneId()));
    }

    public static TimeStruct FromZonedDateTime(ZonedDateTime zonedDateTime) {
        Instant utcTime = zonedDateTime.toInstant();
        return new TimeStruct(utcTime, TimeZone.getTimeZone(zonedDateTime.getZone()));
    }
}
