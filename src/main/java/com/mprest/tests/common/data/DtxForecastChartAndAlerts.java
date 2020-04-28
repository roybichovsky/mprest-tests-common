package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DtxForecastChartAndAlerts {
    private ChartGraph forecastsChart;
    private SingleAlert[] forecastAlerts;
}