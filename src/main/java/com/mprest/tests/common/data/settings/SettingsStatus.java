package com.mprest.tests.common.data.settings;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SettingsStatus {
    @JsonProperty("ON") ON,
    @JsonProperty("OFF") OFF,
    @JsonProperty("UNAUTHORIZED") UNAUTHORIZED
}
