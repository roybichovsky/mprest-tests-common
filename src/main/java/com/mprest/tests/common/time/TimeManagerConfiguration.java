package com.mprest.tests.common.time;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "timemanager")
@EqualsAndHashCode
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
public class TimeManagerConfiguration {
    private String timezone;
    private String utcnow;
    private String timeUpdateTopic;
}
