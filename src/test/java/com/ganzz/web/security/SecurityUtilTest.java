package com.ganzz.web.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityUtilTest {

    @BeforeEach
    void setup() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetSessionUser_WithAuthenticatedUser() {
        Authentication auth = mock(UsernamePasswordAuthenticationToken.class);
        when(auth.getName()).thenReturn("testUser");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        try (MockedStatic<SecurityContextHolder> mockedStaticContext = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStaticContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String result = SecurityUtil.getSessionUser();

            assertEquals("testUser", result);
        }
    }

    @Test
    void testGetSessionUser_WithAnonymousUser() {
        Authentication auth = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        );

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        try (MockedStatic<SecurityContextHolder> mockedStaticContext = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStaticContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String result = SecurityUtil.getSessionUser();

            assertNull(result);
        }
    }

    @Test
    void testGetSessionUser_WithNoAuthentication() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> mockedStaticContext = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedStaticContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            String result = SecurityUtil.getSessionUser();

            assertNull(result);
        }
    }
}