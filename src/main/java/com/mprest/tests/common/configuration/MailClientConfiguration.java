package com.mprest.tests.common.configuration;

import com.mprest.tests.common.data.MailProtocolTypes;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "mail-client")
public class MailClientConfiguration {
    private String host;
    private String user;
    private String password;
    private MailProtocolTypes protocolType;
}
