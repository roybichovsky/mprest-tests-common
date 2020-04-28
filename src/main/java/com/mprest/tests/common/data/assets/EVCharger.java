package com.mprest.tests.common.data.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EVCharger extends DERBase {
    private int chargeSocketCount;

    @Builder.Default
    private List<ChargeSocket> chargeSockets;
}