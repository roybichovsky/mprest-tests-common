package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AssetBase implements Asset {
    protected String id;
    private String name;
    private String olsonTimeZone;
    private String type;
    private String vendor;
    private String reference1;
    private String reference2;
}