package com.example.The_Ca_Nhan.Controller.UnitTest;


import com.example.The_Ca_Nhan.Controller.AuthenticationController;
import com.example.The_Ca_Nhan.Controller.CardController;
import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(AuthenticationController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private LoginRequest loginRequest ;
    private LogoutRequest logoutRequest ;
    private AuthenticationResponse authenticationResponse ;


    @BeforeEach
    void init() {
        loginRequest = LoginRequest.builder()
                .userName("test")
                .password("test")
                .build();


        logoutRequest = LogoutRequest.builder()
                .accessToken("alsfhdaksjhfkshdfkshdkshfkdshkfjjkjslkjasdfsdfs")
                .refreshToken("ksagfjhsdgfjhgsdjgfa93827ksdfhksasdljasfdkdffd")
                .build();


        authenticationResponse = AuthenticationResponse.builder()
                .accessToken("alsfhdaksjhfkshdfkshdkshfkdshkfjjkjslkjasdfsdfs")
                .refreshToken("ksagfjhsdgfjhgsdjgfa93827ksdfhksasdljasfdkdffd")
                .build();
    }

    @Test
    void Login_Success () throws Exception {
        Mockito.when(authenticationService.login(any(LoginRequest.class)))
                .thenReturn(authenticationResponse) ;

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(loginRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.accessToken")
                        .value("alsfhdaksjhfkshdfkshdkshfkdshkfjjkjslkjasdfsdfs"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.refreshToken")
                .value("ksagfjhsdgfjhgsdjgfa93827ksdfhksasdljasfdkdffd"));
    }

    @Test
    void Login_Validation_Fail() throws Exception {
        LoginRequest invalid = LoginRequest.builder().build();
        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(invalid);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void Logout_Success () throws Exception {
        Mockito.doNothing().when(authenticationService)
                .logout(any(LogoutRequest.class));

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(logoutRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess")
                        .value("Success")) ;

    }








}
