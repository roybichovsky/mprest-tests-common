package com.mprest.tests.common.utilities;

import com.mprest.tests.common.configuration.BaseClientConfiguration;
import com.mprest.tests.common.configuration.LimitedUserClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

public abstract class LimitedBaseServiceClient extends BaseServiceClient {

    private final LimitedUserClientConfiguration limitedUserClientConfiguration;

    public LimitedBaseServiceClient(Class<?> clazz, SecuredServerConfiguration secured, BaseClientConfiguration clientConfig, LimitedUserClientConfiguration limitedUserClientConfiguration) {
        super(clazz, secured, clientConfig);
        this.limitedUserClientConfiguration = limitedUserClientConfiguration;
    }

    protected String getUserName() {
        return limitedUserClientConfiguration.getUserName();
    }

    protected String getPassword() {
        return limitedUserClientConfiguration.getPassword();
    }

}
