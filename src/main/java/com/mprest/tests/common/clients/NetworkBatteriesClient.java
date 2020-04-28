package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.UploadResult;

public interface NetworkBatteriesClient {
    UploadResult uploadNetworkBatteries(String fileName, byte[] bytes);
}
