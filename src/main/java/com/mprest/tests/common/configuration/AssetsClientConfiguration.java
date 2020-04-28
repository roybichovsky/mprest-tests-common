package com.mprest.tests.common.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "assets-client")
public class AssetsClientConfiguration extends BaseClientConfiguration {
    private String assetContext;
    private String networkConstraintContext;
    private String assetGeo;
}
