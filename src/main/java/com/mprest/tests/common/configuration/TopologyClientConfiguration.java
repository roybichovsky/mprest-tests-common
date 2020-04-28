package com.mprest.tests.common.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "topology")
public class TopologyClientConfiguration extends BaseClientConfiguration {

    @Getter
    @Setter
    private String inputAbsoluteFilesPath;

    @Autowired
    @Getter
    @Setter
    private TopologyFileName fileName;

    @Autowired
    @Getter
    @Setter
    private TopologyRequest request;


    @Autowired
    private TopologySwitch _switch;

    public TopologySwitch getSwitch() {
        return _switch;
    }

    public void setSwitch(TopologySwitch sswitch) {
        _switch = sswitch;
    }

    @Component
    @Configuration
    @Data
    public static class TopologyRequest {
        private int timeoutMs;
        private int retries;
    }

    @Component
    @Configuration
    @Data
    public static class TopologySwitch {

        @NestedConfigurationProperty
        private TopologySwitchMemberConfiguration parent;

        @NestedConfigurationProperty
        private TopologySwitchMemberConfiguration right;

        @NestedConfigurationProperty
        private TopologySwitchMemberConfiguration left;
    }

    @Component
    @Configuration
    @Data
    public static class TopologyFileName {
        private String prefix;
    }
}
