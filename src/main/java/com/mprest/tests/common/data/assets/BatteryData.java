package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatteryData {
    private String batteryVendor;
    private String batteryModel;
    private double batteryNominalMaxCapacityKWh;
    private double batteryEffectiveMaxCapacityKWh;
    private double batteryDepthOfDischargeSOC;
    private double batteryMaxAllowedCapacitySOC;
    private double batteryNominalChargeRateKW;
    private double batteryNominalDischargeRateKW;
}
