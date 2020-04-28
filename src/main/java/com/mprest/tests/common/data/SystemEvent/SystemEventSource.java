package com.mprest.tests.common.data.SystemEvent;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SystemEventSource {
    @JsonProperty("USER") USER,
    @JsonProperty("DERMS") DERMS,
    @JsonProperty("SCADA") SCADA,
    @JsonProperty("Solar Edge") SOLAR_EDGE,
    @JsonProperty("NSB Uploader") NSB_UPLOADER,
    @JsonProperty("EVC Adapter") EVC_ADAPTER,
    @JsonProperty("ICCP Adapter") ICCP_ADAPTER,
    @JsonProperty("OCPP Central System") OCPP_CENTRAL_SYSTEM,
    @JsonProperty("Solar Edge Monitoring Interface") SOLAR_EDGE_MONITORING_INTERFACE,
    @JsonProperty("Solar Edge Command Interface") SOLAR_EDGE_COMMAND_INTERFACE,
    @JsonProperty("Data warehouse") DATA_WAREHOUSE,
    @JsonProperty("EVC Monitoring Command Interface") EVC_MONITORING_COMMAND_INTERFACE,
    @JsonProperty("System") SYSTEM,
    @JsonProperty("GAuthorization") GAUTHORIZATION,
    @JsonProperty("mprestSystemUser") MPREST_SYSTEM_USER;

}
