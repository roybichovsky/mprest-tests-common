package com.mprest.tests.common.data.settings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mprest.tests.common.data.ReadOnlyResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregatedSettings {
    private ReadOnlyResponse mode;
    private SettingsItem[] derTypes;
    private SettingsItem[] triggers;
    private SettingsStatus arbitrage;
    private SettingsStatus flexibilityForUncontrollableDers;
}
