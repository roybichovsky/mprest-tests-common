package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InverterData {
    private String inverterName;
    private String inverterVendor;
    private String inverterModel;
    private Double inverterRatedOutputACPowerKw;
    private Double inverterEfficiencyPercent;
}