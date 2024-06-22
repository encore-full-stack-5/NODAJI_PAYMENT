package com.nodaji.payment.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.*;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentUtilsTest {

    @InjectMocks
    private PaymentUtils paymentUtils;

    @Mock
    private HttpURLConnection connection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(paymentUtils, "paymentSecretKey", "testSecretKey");
    }

    @Test
    void testGetResponseJsonObject_Success() throws IOException, ParseException {
        String responseJson = "{\"status\":\"success\"}";
        InputStream inputStream = new ByteArrayInputStream(responseJson.getBytes());
        when(connection.getInputStream()).thenReturn(inputStream);

        JSONObject response = paymentUtils.getResponseJsonObject(connection, true);

        assertNotNull(response);
        assertEquals("success", response.get("status"));
    }

    @Test
    void testGetResponseJsonObject_Error() throws IOException, ParseException {
        String errorJson = "{\"status\":\"error\"}";
        InputStream errorStream = new ByteArrayInputStream(errorJson.getBytes());
        when(connection.getErrorStream()).thenReturn(errorStream);

        JSONObject response = paymentUtils.getResponseJsonObject(connection, false);

        assertNotNull(response);
        assertEquals("error", response.get("status"));
    }

    @Test
    void testSendRequest() throws IOException {
        String orderId = "12345";
        Long amount = 1000L;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(connection.getOutputStream()).thenReturn(outputStream);

        paymentUtils.sendRequest(orderId, amount, connection);

        String expectedJson = "{\"amount\":1000,\"orderId\":\"12345\"}";
        assertEquals(expectedJson, outputStream.toString("UTF-8"));
    }


    @Test
    void testGetAuthorizations() throws UnsupportedEncodingException {
        String expectedAuthorization = "Basic dGVzdFNlY3JldEtleTo=";
        String authorization = paymentUtils.getAuthorizations();
        assertEquals(expectedAuthorization, authorization);
    }
}