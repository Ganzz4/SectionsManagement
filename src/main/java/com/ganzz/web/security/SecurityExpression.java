package com.ganzz.web.security;

import com.ganzz.web.dto.EventDto;
import com.ganzz.web.dto.SectionDto;
import com.ganzz.web.service.EventService;
import com.ganzz.web.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("securityExpression")
@RequiredArgsConstructor
public class SecurityExpression {
    private final SectionService sectionService;
    private final EventService eventService;

    public boolean canModifySection(Long sectionId) {
        SectionDto section = sectionService.findSectionById(sectionId);
        String currentUser = SecurityUtil.getSessionUser();
        return hasAccess(currentUser, section.getCreatedBy().getUsername());
    }

    public boolean canModifyEvent(Long eventId) {
        EventDto event = eventService.findByEventId(eventId);
        String currentUser = SecurityUtil.getSessionUser();
        return hasAccess(currentUser, event.getSection().getCreatedBy().getUsername());
    }

    private boolean hasAccess(String currentUser, String creatorUsername) {
        return currentUser != null &&
                (currentUser.equals(creatorUsername) || isAdmin());
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}