package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class DERBase extends AssetBase implements DERAsset {
    private boolean controllable;
    private Boolean isAlive;
    private String controlChannel;
    private String monitoringChannel;
}