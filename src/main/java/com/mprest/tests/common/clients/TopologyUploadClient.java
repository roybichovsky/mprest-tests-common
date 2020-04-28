package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.UploadResult;

public interface TopologyUploadClient {

    UploadResult uploadDtxElements (String fileName, byte[] bytes);

    UploadResult uploadOpenPointsExceptions (String fileName, byte[] bytes);

    UploadResult uploadSwitchingSwitchboardConfiguration(String fileName, byte[] bytes);

    UploadResult uploadBusSelectorConfiguration(String fileName, byte[] bytes);

    UploadResult uploadSwitchingIntegrity(String fileName, byte[] bytes);

    UploadResult uploadSplitNotationConfiguration(String fileName, byte[] bytes);

    UploadResult uploadTopologyFile(String fileName, byte[] bytes);

    UploadResult uploadFeederDerms(String fileName, byte[] bytes);

    UploadResult uploadGroupRating(String fileName, byte[] bytes);

    UploadResult uploadSubstationRating(String fileName, byte[] bytes);

    UploadResult uploadAssetRating(String fileName, byte[] bytes);

    UploadResult uploadDoubleBus(String fileName, byte[] bytes);

    UploadResult uploadDermsChangesTopology(String fileName, byte[] bytes);
}
