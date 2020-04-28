package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.TopologyClientConfiguration;
import com.mprest.tests.common.data.TopologyNode;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class TopologyClientImpl extends BaseServiceClient implements TopologyClient {

    TopologyClientImpl(SecuredServerConfiguration securedConfiguration, TopologyClientConfiguration topologyConfiguration) {
        super(TopologyClientImpl.class, securedConfiguration, topologyConfiguration);
    }

    public TopologyNode getMinimalTopology() {
        Future<TopologyNode> futureTopology = getMinimalTopologyAsync();
        return extractAsyncResult(futureTopology, "could not get the minimal topology");
    }

    public Future<TopologyNode> getMinimalTopologyAsync() {
        String url = baseUrl + "/minimal";
        log.debug("Requesting minimal topology from: '{}'", url);

        return super.supplyAsync(() -> {
            Response response = given()
                    .when()
                        .get(url)
                    .then()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                    .extract()
                        .response();
            return response.body().as(TopologyNode.class);
        });
    }
}