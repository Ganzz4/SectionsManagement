package com.ganzz.web.security;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.mapper.SectionMapper;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.service.EventService;
import com.ganzz.web.service.SectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SecurityExpressionTest {
    @Mock
    private SectionService sectionService;
    @Mock
    private EventService eventService;
    private SecurityExpression securityExpression;

    private static final String CREATOR_USERNAME = "creator";
    private static final String ADMIN_USERNAME = "adminUser";
    private static final String OTHER_USERNAME = "otherUser";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    private static final Long TEST_ID = 1L;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        securityExpression = new SecurityExpression(sectionService, eventService);
    }

    private SectionDto createTestSection() {
        UserEntity creator = new UserEntity();
        creator.setUsername(SecurityExpressionTest.CREATOR_USERNAME);
        return SectionDto.builder()
                .createdBy(creator)
                .build();
    }

    private EventDto createTestEvent() {
        EventDto event = new EventDto();
        SectionDto section = createTestSection();
        event.setSection(SectionMapper.mapToSection(section));
        return event;
    }

    private void setAuthentication(String username, String role) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority(role))
                )
        );
    }

    private void setupAnonymousAuthentication() {
        SecurityContextHolder.clearContext();
        Authentication anonymousAuth = new AnonymousAuthenticationToken(
                "key",
                "anonymousUser",
                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
        );
        SecurityContextHolder.getContext().setAuthentication(anonymousAuth);
    }

    @Test
    void testCanModifySectionAsCreator() {
        SectionDto section = createTestSection();
        when(sectionService.findSectionById(TEST_ID)).thenReturn(section);
        setAuthentication(CREATOR_USERNAME, ROLE_USER);

        boolean result = securityExpression.canModifySection(TEST_ID);

        assertTrue(result);
        verify(sectionService).findSectionById(TEST_ID);
    }

    @Test
    void testCanModifySectionAsAdmin() {
        SectionDto section = createTestSection();
        when(sectionService.findSectionById(TEST_ID)).thenReturn(section);
        setAuthentication(ADMIN_USERNAME, ROLE_ADMIN);

        boolean result = securityExpression.canModifySection(TEST_ID);

        assertTrue(result);
        verify(sectionService).findSectionById(TEST_ID);
    }

    @Test
    void testCanModifySectionAsOtherUser() {
        SectionDto section = createTestSection();
        when(sectionService.findSectionById(TEST_ID)).thenReturn(section);
        setAuthentication(OTHER_USERNAME, ROLE_USER);

        boolean result = securityExpression.canModifySection(TEST_ID);

        assertFalse(result);
        verify(sectionService).findSectionById(TEST_ID);
    }

    @Test
    void testCanModifyEventAsCreator() {
        EventDto event = createTestEvent();
        when(eventService.findByEventId(TEST_ID)).thenReturn(event);
        setAuthentication(CREATOR_USERNAME, ROLE_USER);

        boolean result = securityExpression.canModifyEvent(TEST_ID);

        assertTrue(result);
        verify(eventService).findByEventId(TEST_ID);
    }

    @Test
    void testCanModifyEventAsAdmin() {
        EventDto event = createTestEvent();
        when(eventService.findByEventId(TEST_ID)).thenReturn(event);
        setAuthentication(ADMIN_USERNAME, ROLE_ADMIN);

        boolean result = securityExpression.canModifyEvent(TEST_ID);

        assertTrue(result);
        verify(eventService).findByEventId(TEST_ID);
    }

    @Test
    void testCanModifyEventAsOtherUser() {
        EventDto event = createTestEvent();
        when(eventService.findByEventId(TEST_ID)).thenReturn(event);
        setAuthentication(OTHER_USERNAME, ROLE_USER);

        boolean result = securityExpression.canModifyEvent(TEST_ID);

        assertFalse(result);
        verify(eventService).findByEventId(TEST_ID);
    }

    @Test
    void testIsAdmin() {
        SectionDto section = createTestSection();
        when(sectionService.findSectionById(TEST_ID)).thenReturn(section);
        setAuthentication(ADMIN_USERNAME, ROLE_ADMIN);

        boolean result = securityExpression.canModifySection(TEST_ID);

        assertTrue(result);
        verify(sectionService).findSectionById(TEST_ID);
    }

    @Test
    void testAccessWithNullAuthentication() {
        setupAnonymousAuthentication();
        SectionDto section = createTestSection();
        when(sectionService.findSectionById(TEST_ID)).thenReturn(section);

        boolean result = securityExpression.canModifySection(TEST_ID);

        assertFalse(result);
        verify(sectionService).findSectionById(TEST_ID);
    }
}