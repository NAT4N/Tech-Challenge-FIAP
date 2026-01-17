package com.fiap.techchallenge14.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    private final SwaggerConfig config = new SwaggerConfig();

    @Test
    void customOpenAPI_ShouldConfigureInfoAndAuthorizationSecurityScheme() {
        OpenAPI openAPI = config.customOpenAPI();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());

        assertEquals("Tech Challenge API", openAPI.getInfo().getTitle());
        assertEquals("1.0.0", openAPI.getInfo().getVersion());
        assertEquals("Documentação da API", openAPI.getInfo().getDescription());

        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("Authorization"));

        SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("Authorization");
        assertNotNull(scheme);

        assertEquals(SecurityScheme.Type.APIKEY, scheme.getType());
        assertEquals(SecurityScheme.In.HEADER, scheme.getIn());
        assertEquals("Authorization", scheme.getName());
    }

    @Test
    void userApi_ShouldConfigureGroupedOpenApi() {
        GroupedOpenApi grouped = config.userApi();

        assertNotNull(grouped);
        assertEquals("API", grouped.getGroup());
    }
}
