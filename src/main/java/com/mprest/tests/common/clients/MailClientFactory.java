package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.MailClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static org.junit.Assert.fail;

@Component
@Configuration
public class MailClientFactory {

    @Bean
    @Autowired
    public MailClient buildMailClient(MailClientConfiguration mailClientConfiguration) {
        switch (mailClientConfiguration.getProtocolType()) {
            case IMAP:
                return new MailClientIMAPImpl(mailClientConfiguration.getHost(),
                        mailClientConfiguration.getUser(),
                        mailClientConfiguration.getPassword());
            default:
                fail("Mail client type not supported");
                return null;
        }
    }
}
