package com.ganzz.web.controller;

import com.ganzz.web.dto.RegistrationDto;
import com.ganzz.web.model.UserEntity;
import com.ganzz.web.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLoginForm_ShouldReturnLoginViewAndAddUserToModel() {
        String viewName = authController.getLoginForm(model);

        verify(model).addAttribute(eq("user"), any(RegistrationDto.class));
        assertEquals("login", viewName);
    }

    @Test
    void getRegisterForm_ShouldReturnRegisterViewAndAddUserToModel() {
        String viewName = authController.getRegisterForm(model);

        verify(model).addAttribute(eq("user"), any(RegistrationDto.class));
        assertEquals("register", viewName);
    }

    @Test
    void register_WhenEmailExists_ShouldRedirectToRegisterWithFailure() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("existing@email.com");

        UserEntity existingUser = new UserEntity();
        existingUser.setEmail("existing@email.com");

        when(userService.findByEmail(anyString())).thenReturn(existingUser);

        String viewName = authController.register(registrationDto, bindingResult, model);

        assertEquals("redirect:/register?fail", viewName);
        verify(userService, never()).saveUser(any(RegistrationDto.class));
    }

    @Test
    void register_WhenUsernameExists_ShouldRedirectToRegisterWithFailure() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setUsername("existingUser");

        UserEntity existingUser = new UserEntity();
        existingUser.setUsername("existingUser");

        when(userService.findByEmail(anyString())).thenReturn(null);
        when(userService.findByUsername(anyString())).thenReturn(existingUser);

        String viewName = authController.register(registrationDto, bindingResult, model);

        assertEquals("redirect:/register?fail", viewName);
        verify(userService, never()).saveUser(any(RegistrationDto.class));
    }

    @Test
    void register_WhenValidationErrors_ShouldReturnRegisterView() {
        RegistrationDto registrationDto = new RegistrationDto();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.register(registrationDto, bindingResult, model);

        assertEquals("register", viewName);
        verify(model).addAttribute("user", registrationDto);
        verify(userService, never()).saveUser(any(RegistrationDto.class));
    }

    @Test
    void register_WhenSuccessful_ShouldRedirectToSectionsWithSuccess() {
        RegistrationDto registrationDto = new RegistrationDto();
        registrationDto.setEmail("new@email.com");
        registrationDto.setUsername("newUser");

        when(userService.findByEmail(anyString())).thenReturn(null);
        when(userService.findByUsername(anyString())).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.register(registrationDto, bindingResult, model);

        assertEquals("redirect:/sections?success", viewName);
        verify(userService).saveUser(registrationDto);
    }
}