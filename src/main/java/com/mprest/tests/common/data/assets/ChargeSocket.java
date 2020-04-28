package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeSocket {
    private String id;
    private String type;
}