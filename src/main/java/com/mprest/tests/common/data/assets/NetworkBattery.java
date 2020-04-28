package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkBattery extends DERBase {
    private String model;
    private Double nominalCapacityKwh;
    private Double depthOfDischargeKwh;
    private Double maxAllowedCapacityKwh;
    private Double nominalChargeRateKw;
    private Double nominalDischargeRateKw;
    private boolean arbitrageStepContinuous;
    private Integer dischargeHourStart;
    private Integer chargeHourStart;
}