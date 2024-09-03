package io.quarkus.ts.http.restclient.reactive;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.core.Options.ChunkedEncodingPolicy.NEVER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.Fault;

import io.quarkus.test.bootstrap.RestService;
import io.quarkus.test.scenarios.QuarkusScenario;
import io.quarkus.test.services.QuarkusApplication;

@QuarkusScenario
public class ReactiveRestClientRetryIT {

    private static final WireMockServer mockServer;

    static {
        mockServer = new WireMockServer(WireMockConfiguration.options()
                .dynamicPort()
                .useChunkedTransferEncoding(NEVER));
        mockServer.stubFor(WireMock.get(WireMock.urlEqualTo("/client/async"))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)));

        mockServer.start();
    }

    @QuarkusApplication
    static RestService app = new RestService()
            .withProperty("client.endpoint/mp-rest/url", mockServer::baseUrl);

    @Test
    void shouldPerformRetryOfFailingBlockingClientCall() {
        app.given().header("REQUEST_ID", 121)
                .get("/server/async")
                .then()
                .statusCode(500);

        assertEquals(4, mockServer.getServeEvents().getServeEvents().stream().count());
    }

    @AfterAll
    static void afterAll() {
        mockServer.stop();
    }
}
