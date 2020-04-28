package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.ChartGraphClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.ChartGraph;
import com.mprest.tests.common.utilities.BaseServiceClient;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class ChartGraphClientImpl extends BaseServiceClient implements ChartGraphClient {

    ChartGraphClientImpl(SecuredServerConfiguration securedConfiguration, ChartGraphClientConfiguration chartGraphClientConfiguration) {
        super(ChartGraphClientImpl.class, securedConfiguration, chartGraphClientConfiguration);
    }

    @Override
    public Optional<ChartGraph> getChart(String chartGraphName, String resourceId, Instant dateTimeAnchor) {
        Future<Optional<ChartGraph>> futureChart = getChartAsync(chartGraphName, resourceId, dateTimeAnchor);
        return extractOptionalAsyncResult(futureChart, "could not get " + chartGraphName + " Chart");
    }

    @Override
    public Future<Optional<ChartGraph>> getChartAsync(String chartGraphName, String id, Instant dateTimeAnchor) {
        String url = baseUrl + "/" + chartGraphName + "/" + id;
        log.debug("getting '{}' chart data from URL '{}'", chartGraphName, url);

        return getOptionalChart(url, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getFlexibilityChart(String resourceId, Instant dateTimeAnchor) {
        return getChart("flexibility", resourceId, dateTimeAnchor);
    }

    @Override
    public Future<Optional<ChartGraph>> getFlexibilityChartAsync(String resourceId, Instant dateTimeAnchor) {
        return getChartAsync("flexibility", resourceId, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getMarketPriceChart(String resourceId, Instant dateTimeAnchor) {
        return getChart("marketPrice", resourceId, dateTimeAnchor);
    }

    @Override
    public Future<Optional<ChartGraph>> getMarketPriceChartAsync(String resourceId, Instant dateTimeAnchor) {
        return getChartAsync("marketPrice", resourceId, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getLoadForecastChart(String resourceId, Instant dateTimeAnchor) {
        return getChart("loadForecast", resourceId, dateTimeAnchor);
    }

    @Override
    public Future<Optional<ChartGraph>> getLoadForecastChartAsync(String resourceId, Instant dateTimeAnchor) {
        return getChartAsync("loadForecast", resourceId, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getDerPlanChart(String resourceId, Instant dateTimeAnchor) {
        return getChart("derPlan", resourceId, dateTimeAnchor);
    }

    @Override
    public Future<Optional<ChartGraph>> getDerPlanChartAsync(String resourceId, Instant dateTimeAnchor) {
        return getChartAsync("derPlan", resourceId, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getAvailableEnergyChart(String resourceId, Instant dateTimeAnchor) {
        return getChart("availableEnergy", resourceId, dateTimeAnchor);
    }

    @Override
    public Future<Optional<ChartGraph>> geAvailableEnergyChartAsync(String resourceId, Instant dateTimeAnchor) {
        return getChartAsync("availableEnergy", resourceId, dateTimeAnchor);
    }

    @Override
    public Optional<ChartGraph> getDemandResponseChart(Instant dateTimeAnchor) {
        Future<Optional<ChartGraph>> futureDemandResponseChart = getDemandResponseChartAsync(dateTimeAnchor);
        return extractOptionalAsyncResult(futureDemandResponseChart, "could not get Demand Response Chart ");
    }

    @Override
    public Future<Optional<ChartGraph>> getDemandResponseChartAsync(Instant dateTimeAnchor) {
        String url = baseUrl + "/demandResponse";
        log.debug("getting demand response chart data from URL '{}'", url);

        return getOptionalChart(url, dateTimeAnchor);
    }

    private Future<Optional<ChartGraph>> getOptionalChart(String url, Instant dateTimeAnchor) {
        return super.supplyAsync(() -> {
            Response response =
                    given()
                        .queryParam("dateTime", dateTimeAnchor.toString())
                    .when()
                        .get(url)
                    .then()
                        .contentType(ContentType.JSON)
                    .assertThat()
                        .statusCode(HttpStatus.OK.value())
                    .extract()
                        .response();

            ResponseBody body = response.body();
            if (body == null)
                return Optional.empty();

            return Optional.of(body.as(ChartGraph.class));
        });
    }
}
