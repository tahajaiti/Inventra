package com.hnaya.inventra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthenticationEntryPointTest {

    private JwtAuthenticationEntryPoint entryPoint;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        entryPoint = new JwtAuthenticationEntryPoint();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Should set unauthorized status and JSON error message in response")
    void commence_ShouldReturnUnauthorizedError() throws IOException {
        AuthenticationException authException = new BadCredentialsException("Full authentication is required to access this resource");

        entryPoint.commence(request, response, authException);


        assertEquals("application/json", response.getContentType());

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        String expectedBody = "{\"error\": \"Unauthorized\", \"message\": \"Full authentication is required to access this resource\"}";
        assertEquals(expectedBody, response.getContentAsString());
    }
}