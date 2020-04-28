package com.mprest.tests.common.data.SystemEvent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemEvent {
    private SystemEventSource subject;
    private SystemEventCategory category;
    private String action;
    private String user;
    private String location;
    private Instant utcTime;
    private Map<String, Object> details;
}