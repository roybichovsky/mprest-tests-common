package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.TopologyUploadClientConfiguration;
import com.mprest.tests.common.data.UploadResult;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class TopologyUploadClientImpl extends BaseServiceClient implements TopologyUploadClient {

    TopologyUploadClientImpl(SecuredServerConfiguration secured, TopologyUploadClientConfiguration clientConfig) {
        super(TopologyUploadClientImpl.class, secured, clientConfig);
    }

    @Override
    public UploadResult uploadDtxElements(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "dtxElements");
    }

    @Override
    public UploadResult uploadOpenPointsExceptions(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "openPointsExceptions");
    }

    @Override
    public UploadResult uploadSwitchingSwitchboardConfiguration(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "switchingSWBDConfiguration");
    }

    @Override
    public UploadResult uploadBusSelectorConfiguration(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "busSelector");
    }

    @Override
    public UploadResult uploadSplitNotationConfiguration(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "splitNotationConfiguration");
    }

    @Override
    public UploadResult uploadSwitchingIntegrity(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "switchingIntegrity");
    }

    @Override
    public UploadResult uploadTopologyFile(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "dermsTopology");
    }

    @Override
    public UploadResult uploadFeederDerms(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "feederDerms");
    }

    @Override
    public UploadResult uploadGroupRating(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "groupRatings");
    }

    @Override
    public UploadResult uploadSubstationRating(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "substationRatings");
    }

    @Override
    public UploadResult uploadAssetRating(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "assetRatings");
    }

    @Override
    public UploadResult uploadDoubleBus(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "doubleBus");
    }

    @Override
    public UploadResult uploadDermsChangesTopology(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "dermsChangesTopology");
    }

    private UploadResult uploadAsync(String fileName, byte[] bytes, String fileUrl) {
        Future<UploadResult> file = uploadFile(fileName, bytes, fileUrl);
        return extractAsyncResult(file, "Upload failed", Duration.ofSeconds(120), null);
    }

    private Future<UploadResult> uploadFile(String fileName, byte[] bytes, String fileUrl) {
        String url = baseUrl + "/" + fileUrl;
        log.debug("posting to URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .multiPart("file", fileName, bytes)
                            .when()
                                    .post(url)
                            .then()
                            .extract()
                                    .response();

                    Map result = response.body().as(Map.class);
                    log.info("Result from upload file '{}'", result.toString());

                    // Result object can be one of the following:
                    // {
                    //  "message": "string",
                    //  "properties": {},
                    //  "success": true
                    // }
                    // =========  OR  =========
                    // {
                    //  "timestamp": "2020-03-03T13:03:15.455+0000",
                    //  "status": 500,
                    //  "error": "Internal Server Error",
                    //  "exception": "java.lang.RuntimeException",
                    //  "message": "failed to parse matching exception file hard pairs: '0',  ignored : '0'",
                    //  "path": "/rest/feederTopology/topologyUploads/matchingExceptions"
                    // }
                    //
                    // Error message content will match the error case.

                    boolean success = result.get("success") != null && Boolean.parseBoolean(result.get("success").toString());
                    String message = result.get("message") == null ? "" : result.get("message").toString();

                    return UploadResult.builder()
                            .success(success)
                            .message(message)
                            .build();
                }
        );
    }
}
