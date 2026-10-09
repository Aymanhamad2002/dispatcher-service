package com.polarbookshop.dispatcherservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.cloud.function.definition=",
        "spring.cloud.stream.function.autodetect=false"
})
class DispatcherObservabilityTests {

    @LocalServerPort
    private int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void healthProbesAreAvailableWithoutAuthentication() throws Exception {
        for (String probe : new String[] {"liveness", "readiness"}) {
            var response = get("/actuator/health/" + probe);
            assertThat(response.statusCode()).isEqualTo(200);
            assertThat(response.body()).contains("\"status\":\"UP\"");
        }
    }

    @Test
    void prometheusMetricsIncludeTheApplicationTag() throws Exception {
        var response = get("/actuator/prometheus");
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("application=\"dispatcher-service\"");
    }

    private HttpResponse<String> get(String path) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path))
                .GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
