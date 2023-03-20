package com.app.cindy.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.stereotype.Component;
import springfox.documentation.oas.web.OpenApiTransformationContext;
import springfox.documentation.oas.web.WebMvcOpenApiTransformationFilter;
import springfox.documentation.spi.DocumentationType;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
public class Workaround implements WebMvcOpenApiTransformationFilter {

    @Override
    public OpenAPI transform(OpenApiTransformationContext<HttpServletRequest> context) {
        OpenAPI openApi = context.getSpecification();

        Server localServer = new Server();
        localServer.setDescription("local");
        localServer.setUrl("http://localhost:9000");

        Server testServer = new Server();
        testServer.setDescription("test");
        testServer.setUrl("https://dev.runwayserver.shop");

        Server prodServer = new Server();
        prodServer.setDescription("prod");
        prodServer.setUrl("https://prod.runwayserver.shop");
        openApi.setServers(Arrays.asList(localServer, testServer,prodServer));


        return openApi;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.OAS_30);
    }
}