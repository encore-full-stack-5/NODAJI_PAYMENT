package com.nodaji.payment.service;

import com.nodaji.payment.dto.response.PaymentSuccessResponseDto;
import com.nodaji.payment.global.domain.entity.Account;
import com.nodaji.payment.global.domain.entity.PaymentHistory;
import com.nodaji.payment.global.domain.exception.AccountNotFoundException;
import com.nodaji.payment.global.domain.repository.AccountRepository;
import com.nodaji.payment.global.domain.repository.PaymentHistoryRepository;
import com.nodaji.payment.utils.PaymentUtils;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    private static final String PAYMENT_SECRET_KEY = "testSecretKey";
    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private PaymentUtils paymentUtils;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private JSONObject jsonObject;
    private String userId;

    @BeforeEach
    public void setUp() {
        jsonObject = new JSONObject();
        jsonObject.put("orderId", "12345");
        jsonObject.put("type", "type1");
        jsonObject.put("method", "credit");
        jsonObject.put("approvedAt", "2023-06-17T00:00:00");
        jsonObject.put("totalAmount", "1000");
        jsonObject.put("status", "approved");
        jsonObject.put("orderName", "orderName1");
        userId = "user123";
    }

    @Test
    @DisplayName("결제 거래내역 생성 테스트")
    @Transactional
    public void createPaymentHistoryTest(){
        PaymentHistory paymentHistory = new PaymentHistory().toEntity(jsonObject, userId);
        paymentService.createPaymentHistory(jsonObject, userId);
        verify(paymentHistoryRepository, times(1)).save(any(PaymentHistory.class));
    }

    @Test
    @DisplayName("계좌 존재 검증 테스트")
    @Transactional
    public void accountExistTest() throws Exception {
        // Given
        when(accountService.isExistAccount(anyString())).thenReturn(false);

        assertThrows(AccountNotFoundException.class, () -> {
            paymentService.processPayment("user1", "order1", 100L, "paymentKey");
        });
    }

    @Test
    @DisplayName("결제 성공 테스트")
    @Transactional
    public void testSuccessfulPayment() throws Exception {

        JSONObject mockJsonObject = new JSONObject();
        mockJsonObject.put("orderId", "mDkwA97bvLXaMyl5tZRpY");
        mockJsonObject.put("type", "NORMAL");
        mockJsonObject.put("orderName", "토스 티셔츠 외 2건");
        mockJsonObject.put("method", "간편결제");
        mockJsonObject.put("approvedAt", "2024-06-17T12:06:28+09:00");
        mockJsonObject.put("totalAmount", 50000);
        mockJsonObject.put("status", "DONE");

        when(accountService.isExistAccount(anyString())).thenReturn(true);
        when(paymentUtils.getAuthorizations()).thenReturn("authorizations");
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(paymentUtils.getHttpURLConnection(anyString(), anyString())).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(200);
        when(paymentUtils.getResponseJsonObject(any(), anyBoolean())).thenReturn(mockJsonObject);

        PaymentSuccessResponseDto response = (PaymentSuccessResponseDto) paymentService.processPayment("user1", "order1", 100L, "paymentKey");

        verify(accountService, times(1)).depositPoint("user1", 100L);
        verify(accountService, times(1)).createDepositHistory("user1", 100L);
        verify(paymentUtils, times(1)).sendRequest("order1", 100L, mockConnection);
        assertNotNull(response);
    }
}