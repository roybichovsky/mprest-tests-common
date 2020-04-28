package com.mprest.tests.common.data.SystemEvent;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SystemEventAction {
    @JsonProperty("Add") ADD,
    @JsonProperty("Update") UPDATE,
    @JsonProperty("Delete") DELETE,
    @JsonProperty("Switching Event") SWITCHING_EVENT,
    @JsonProperty("File Loaded") FILE_LOADED,
    @JsonProperty("Login") LOGIN,
    @JsonProperty("Logout") LOGOUT,
    @JsonProperty("New DER was added") DER_ADDED,
    @JsonProperty("Switch to Read-only (RO) mode") SWITCH_TO_READONLY,
    @JsonProperty("Switch from Read-only (RO) mode") SWITCH_FROM_READONLY,
    @JsonProperty("Action System Event") ACTION_SYSTEM_EVENT,
    @JsonProperty("Disable/Enable DER type") DISABLE_ENABLE_DER_TYPE,
    @JsonProperty("Connectivity Update") CONNECTIVITY_UPDATE,
    @JsonProperty("(READ ONLY) - Scheduled plan invoked : charge") READ_ONLY_SCHEDULED_PLAN_CHARGE,
    @JsonProperty("(READ ONLY) - Scheduled plan invoked : off") READ_ONLY_SCHEDULED_PLAN_CHARGE_OFF,
    @JsonProperty("READ_ONLY_STATE_CHANGED") READ_ONLY_STATE_CHANGED,
    @JsonProperty("Activate") ACTIVATE,
    @JsonProperty("Deactivate") DEACTIVATE,
    @JsonProperty("Procedure Invoked - Success") PROCEDURE_INVOKED_SUCCESS,
    @JsonProperty("Procedure Invoked - Failure") PROCEDURE_INVOKED_FAILED,
    @JsonProperty("Guthorization CRUD Operation") GAUTHORIZATION_CRUD_OPERATION,
    @JsonProperty("Guthorization Syncing User") GAUTHORIZATION_SYNCING_USER,
    @JsonProperty("Pending Commands") PENDING_COMMANDS,
    @JsonProperty("Alert Created") ALERT_CREATED,
    @JsonProperty("Trigger Planning Engine") TRIGGER_PLANNING_ENGINE,
    @JsonProperty("DER Plan Successfully Generated") DER_PLAN_SUCCESSFULLY_GENERATED,
    @JsonProperty("Trigger Planning Engine Manually") TRIGGER_PLANNING_ENGINE_MANUALLY;







}
