package com.ganzz.web.security;

import com.ganzz.web.model.Role;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setUsername("testuser");
        testUser.setPassword("encodedPassword");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        testUser.setRoles(List.of(userRole, adminRole));
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        when(userRepository.findFirstByUsername("testuser")).thenReturn(testUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");

        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());

        List<SimpleGrantedAuthority> expectedAuthorities = testUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        assertEquals(expectedAuthorities.size(), userDetails.getAuthorities().size());
        assertEquals(
                expectedAuthorities.stream().map(Object::toString).collect(Collectors.toSet()),
                userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet())
        );
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        when(userRepository.findFirstByUsername("nonexistentuser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistentuser"));
    }

    @Test
    void loadUserByUsername_UserExistsWithNoRoles_ReturnsUserDetailsWithEmptyAuthorities() {
        UserEntity userWithNoRoles = new UserEntity();
        userWithNoRoles.setUsername("norolesuser");
        userWithNoRoles.setPassword("encodedPassword");
        userWithNoRoles.setRoles(List.of());

        when(userRepository.findFirstByUsername("norolesuser")).thenReturn(userWithNoRoles);

        UserDetails userDetails = userDetailsService.loadUserByUsername("norolesuser");

        assertNotNull(userDetails);
        assertEquals("norolesuser", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void verifyInteractionWithUserRepository() {
        when(userRepository.findFirstByUsername("testuser")).thenReturn(testUser);

        userDetailsService.loadUserByUsername("testuser");

        verify(userRepository, times(1)).findFirstByUsername("testuser");
    }
}