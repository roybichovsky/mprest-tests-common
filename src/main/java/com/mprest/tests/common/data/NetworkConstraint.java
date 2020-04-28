package com.mprest.tests.common.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkConstraint {
    private String assetId;
    private int incomingFeeders;
    private Double cyclic;
    private Double soft;
    private Double hard;
    private String constraintsUnit;

    @Override
    public String toString() {
        StringBuffer retVal = new StringBuffer();
        if (getCyclic() != null) {
            retVal.append("Cyclic: ").append(getCyclic());
        } else {
            retVal.append("Hard: ").append(getHard()).append(" Soft: ").append(getSoft());
        }
        return retVal.toString();
    }
}
