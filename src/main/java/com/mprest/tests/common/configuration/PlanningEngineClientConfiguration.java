package com.mprest.tests.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "planning-engine")
public class PlanningEngineClientConfiguration extends BaseClientConfiguration {
    private long statusPollingIntervalSeconds;
    private long maxTimeoutSeconds;

    public long getStatusPollingInterval() {
        return statusPollingIntervalSeconds;
    }

    public void setStatusPollingInterval(long statusPollingInterval) {
        this.statusPollingIntervalSeconds = statusPollingInterval;
    }

    public long getMaxTimeout() {
        return maxTimeoutSeconds;
    }

    public void setMaxTimeout(long maxTimeout) {
        this.maxTimeoutSeconds = maxTimeout;
    }
}
