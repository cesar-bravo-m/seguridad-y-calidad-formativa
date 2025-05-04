package com.example.formativa.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.formativa.backend.Constants;

@SpringBootTest
@AutoConfigureMockMvc
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

     @Test
     @DisplayName("Endpoints públicos deben ser accesibles sin autenticación")
     void testPublicEndpoints() throws Exception {
         mockMvc.perform(get("/"))
                .andExpect(status().isOk());

         mockMvc.perform(get("/register"))
                .andExpect(status().isOk());

         mockMvc.perform(get(Constants.LOGIN_URL))
                .andExpect(status().isOk());

         mockMvc.perform(get("/css/styles.css"))
                .andExpect(status().isOk());
     }

     @Test
     @DisplayName("Endpoints protegidos deben requerir autenticación")
     void testProtectedEndpoints() throws Exception {
         mockMvc.perform(get("/profile"))
                .andExpect(status().isForbidden());

         mockMvc.perform(get("/search"))
                .andExpect(status().isForbidden());

         mockMvc.perform(get("/details/1"))
                .andExpect(status().isForbidden());
     }
} 