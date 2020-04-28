package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.settings.AggregatedSettings;

import java.util.concurrent.Future;

public interface AggregatedSettingsClient {
    AggregatedSettings getSettings();
    Future<AggregatedSettings> getSettingsAsync();
}
