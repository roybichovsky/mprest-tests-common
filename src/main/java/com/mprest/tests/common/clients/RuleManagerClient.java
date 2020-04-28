package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.RuleEngine.RuleData;

import java.util.Optional;
import java.util.concurrent.Future;


public interface RuleManagerClient {

    Optional<RuleData> getRule(String id);
    Future<Optional<RuleData>> getRuleAsync(String id);

    Optional<RuleData> createRule(RuleData rule);
    Future<Optional<RuleData>> createRuleAsync(RuleData rule);

    Boolean deleteRule(String id);
    Future<Boolean> deleteRuleAsync(String id);
}
