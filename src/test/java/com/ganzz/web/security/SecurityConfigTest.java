package com.ganzz.web.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String[] TEST_PUBLIC_URLS = {"/login", "/register", "/sections", "/events", "/assets", "/css", "/js"};

    @Test
    void publicUrlsShouldBeAccessible() throws Exception {
        for (String url : TEST_PUBLIC_URLS) {
            mockMvc.perform(get(url))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void privateUrlsShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/private-endpoint"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void loginSuccessTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sections"));
    }

    @Test
    void loginFailureTest() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    @WithMockUser
    void logoutTest() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"));
    }
}