package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.GraphDataClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.utilities.BaseServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class GraphDataClientImpl extends BaseServiceClient implements GraphDataClient {

    GraphDataClientImpl(SecuredServerConfiguration securedConfiguration, GraphDataClientConfiguration graphDataClientConfiguration) {
        super(GraphDataClientImpl.class, securedConfiguration, graphDataClientConfiguration);
    }

    @Override
    public Map getGraphData(String resourceId, String graphList) {
        Future<Map> futureGraphData = getGraphDataAsync(resourceId, graphList);
        return extractAsyncResult(futureGraphData, "could not get graph data");
    }

    @Override
    public Future<Map> getGraphDataAsync(String resourceId, String graphList) {
        String url = baseUrl + "/algo/graph-bundle";
        log.debug("getting graph data from URL '{}'", url);

        return super.supplyAsync(() ->
                given()
                        .queryParam("resourceId", resourceId)
                        .queryParam("graphList", graphList)
                        .queryParam("version", -1)
                .when()
                        .get(url)
                .then()
                .assertThat()
                        .statusCode(HttpStatus.OK.value())
                .extract()
                        .response().body().as(Map.class));
    }

}
