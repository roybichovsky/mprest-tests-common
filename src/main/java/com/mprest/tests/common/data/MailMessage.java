package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {

    private String subject;
    private String body;
    private String from;
    private String to;
    private Instant receivedDate;
}
