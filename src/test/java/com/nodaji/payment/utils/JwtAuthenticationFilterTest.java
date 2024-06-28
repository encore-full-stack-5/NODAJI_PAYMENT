package com.nodaji.payment.utils;

import com.nodaji.payment.global.domain.entity.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService authService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_withValidToken() throws ServletException, IOException {
        String token = "validToken";
        String bearerToken = "Bearer " + token;
        UserDto dummyUser = new UserDto("dummyUserId", "dummyEmail@example.com", "Dummy User");

        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtil.parseToken(token)).thenReturn(dummyUser);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(1)).parseToken(token);
        verify(filterChain, times(1)).doFilter(request, response);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assert(authentication != null);
        assert(authentication.getPrincipal().equals(dummyUser));
    }

    @Test
    public void testDoFilterInternal_withoutToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil, times(0)).parseToken(anyString());
        verify(filterChain, times(1)).doFilter(request, response);

        assert(SecurityContextHolder.getContext().getAuthentication() == null);
    }
}
