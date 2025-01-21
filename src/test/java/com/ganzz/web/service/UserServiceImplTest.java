package com.ganzz.web.service;

import com.ganzz.web.dto.RegistrationDto;
import com.ganzz.web.models.Role;
import com.ganzz.web.models.UserEntity;
import com.ganzz.web.repository.RoleRepository;
import com.ganzz.web.repository.UserRepository;
import com.ganzz.web.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private RegistrationDto registrationDto;
    private UserEntity userEntity;
    private Role role;

    @BeforeEach
    void setUp() {
        registrationDto = new RegistrationDto();
        registrationDto.setUsername("testUser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setEmail("test@example.com");

        role = new Role();
        role.setName("USER");
    }

    @Test
    void testSaveUser() {
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);

        userService.saveUser(registrationDto);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(userEntity);

        UserEntity result = userService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testUser")).thenReturn(userEntity);

        UserEntity result = userService.findByUsername("testUser");

        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testUser");
    }
}
