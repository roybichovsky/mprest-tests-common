package com.mprest.tests.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "server.ssl")
public class SecuredServerConfiguration {
    private boolean enabled;
    private String rootHost;
    private int rootPort;
    private String apiManagerHost;
    private int apiManagerPort;
    private String userName;
    private String password;
    private String clientId;
    private String clientSecret;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRootHost() {
        return rootHost;
    }

    public void setRootHost(String rootHost) {
        this.rootHost = rootHost;
    }

    public int getRootPort() {
        return rootPort;
    }

    public void setRootPort(int rootPort) {
        this.rootPort = rootPort;
    }

    public String getApiManagerHost() {
        return apiManagerHost;
    }

    public void setApiManagerHost(String apiManagerHost) {
        this.apiManagerHost = apiManagerHost;
    }

    public int getApiManagerPort() {
        return apiManagerPort;
    }

    public void setApiManagerPort(int apiManagerPort) {
        this.apiManagerPort = apiManagerPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
