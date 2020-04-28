package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.feeders.FeederNode;
import com.mprest.tests.common.data.feeders.FeederTxWeight;
import io.vavr.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public interface FeederTopologyClient {
    Boolean triggerTopologyCalculations();
    Future<Boolean> triggerTopologyCalculationsAsync();

    List<Tuple2<String, String>> getTxToFeedersMapping();
    Future<List<Tuple2<String, String>>> getTxToFeedersMappingAsync();

    List<String> getTxToFeedersMappingForTx(String txId);
    Future<List<String>> getTxToFeedersMappingForTxAsync(String txId);

    List<Tuple2<String, String>> getFeedersToTxMapping(String match);
    Future<List<Tuple2<String, String>>> getFeedersToTxMappingAsync(String match);

    List<String> getFeedersToTxMappingForFeeder(String feederId);
    Future<List<String>> getFeedersToTxMappingForFeederAsync(String feederId);

    List<FeederTxWeight> getFeedersTxWithWeight(String match);
    Future<List<FeederTxWeight>> getFeedersTxWithWeightAsync(String match);

    List<Tuple2<String, Set<String>>> getMultipleFedTransformers();
    Future<List<Tuple2<String, Set<String>>>> getMultipleFedTransformersAsync();

    List<Tuple2<String, Integer>> getFeederWeight();
    Future<List<Tuple2<String, Integer>>> getFeederWeightAsync();

    List<Tuple2<String, Integer>> getFeederWeightByFeeder(String feederId);
    Future<List<Tuple2<String, Integer>>> getFeederWeightByFeederAsync(String feederId);

    List<FeederNode> getEnergizedFeeders(Set<String> feeders);
    Future<List<FeederNode>> getEnergizedFeedersAsync(Set<String> feeders);
}
