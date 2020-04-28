package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mprest.tests.common.data.KeyInfo;

public interface DERAsset extends Asset {
    boolean isControllable();

    Boolean getIsAlive();
    void setIsAlive(Boolean isAlive);

    String getControlChannel();
    void setControlChannel(String controlChannel);

    String getMonitoringChannel();
    void setMonitoringChannel(String monitoringChannel);

    @JsonIgnore
    default KeyInfo getDerKey() {
        return KeyInfo.builder().id(this.getId()).type(this.getType()).vendor(this.getVendor()).build();
    }
}