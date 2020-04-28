package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GenieEntityDataCollectionClient extends ArrayList<GenieEntityDataClient> {
}
