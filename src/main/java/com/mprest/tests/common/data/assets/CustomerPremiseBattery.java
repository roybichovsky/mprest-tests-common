package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerPremiseBattery extends DERBase {
    private Boolean hasInverter;
    private Boolean hasBattery;
    private Boolean hasPV;
    private String pvVendor;
    private String pvModel;
    private List<InverterData> inverterDataList;
    private List<BatteryData> batteryDataList;
    private Integer numOfBatteries;
    private Double pvMaxPowerKw;
}