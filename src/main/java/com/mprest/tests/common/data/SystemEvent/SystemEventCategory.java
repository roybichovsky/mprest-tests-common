package com.mprest.tests.common.data.SystemEvent;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SystemEventCategory {
    @JsonProperty("Login/Logout") Login_Logout,
    @JsonProperty("User Defined Constraints") USER_DEFINED_CONSTRAINTS,
    @JsonProperty("Demand Response") DEMAND_RESPONSE,
    @JsonProperty("Control changes") CONTROL_CHANGES,
    @JsonProperty("Settings change") SETTING_CHANGES,
    @JsonProperty("Topology") TOPOLOGY,
    @JsonProperty("Connectivity") CONNECTIVITY,
    @JsonProperty("Planning Engine") PE,
    @JsonProperty("Alert") ALERT,
    @JsonProperty("Command Execution") COMMAND_EXECUTION,
    @JsonProperty("ReadOnlyState") READ_ONLY_STATE,
    @JsonProperty("Adapters Connectivity") ADAPTERS_CONNECTIVITY,
    @JsonProperty("Device Registration") DEVICE_REGISTRATION,
    @JsonProperty("DER Association") DER_ASSOCIATION,
    @JsonProperty("File Uploads") FILE_UPLOADS,
    @JsonProperty("Procedure") PROCEDURE,
    @JsonProperty("Rule") RULE,
    @JsonProperty("Role") ROLE;

}
