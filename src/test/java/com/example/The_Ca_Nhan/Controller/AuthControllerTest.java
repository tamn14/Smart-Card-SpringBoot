package com.example.The_Ca_Nhan.Controller;

import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any ;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("john.doe");
        loginRequest.setPassword("123");

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .tokenType("Bearer")
                .expiresIn(3600)
                .authenticated(true)
                .build();

        Mockito.when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(loginRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("dummyAccessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.authenticated").value(true));
    }
}
