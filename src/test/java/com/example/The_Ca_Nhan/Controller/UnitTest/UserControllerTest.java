package com.example.The_Ca_Nhan.Controller.UnitTest;

import com.example.The_Ca_Nhan.Controller.UserController;
import com.example.The_Ca_Nhan.DTO.Request.UsersRequest;
import com.example.The_Ca_Nhan.DTO.Request.UsersUpdateRequest;
import com.example.The_Ca_Nhan.DTO.Response.UsersResponse;
import com.example.The_Ca_Nhan.Service.Interface.UsersInterface;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersInterface usersInterface;

    private UsersRequest usersRequest;
    private UsersUpdateRequest usersUpdateRequest;
    private UsersResponse usersResponse;
    private List<UsersResponse> usersResponseList;

    @BeforeEach
    void initData() {
        usersRequest = UsersRequest.builder()
                .userName("john.doe")
                .password("password123")
                .lastName("Doe")
                .firstName("John")
                .email("john.doe@example.com")
                .address("123 Main Street")
                .build();

        usersUpdateRequest = UsersUpdateRequest.builder()
                .lastName("Smith")
                .firstName("Johnathan")
                .email("john.smith@example.com")
                .address("456 Elm Street, HCM City")
                .build();

        usersResponse = UsersResponse.builder()
                .id(1)
                .keycloakId("keycloak-123")
                .lastName("Doe")
                .firstName("John")
                .email("john.doe@example.com")
                .address("123 Main Street")
                .createAt(LocalDate.of(2024, 5, 20))
                .enable(true)
                .build();


        usersResponseList = List.of(
                usersResponse,
                UsersResponse.builder()
                        .id(2)
                        .keycloakId("keycloak-67890")
                        .lastName("Nguyen")
                        .firstName("An")
                        .email("an.nguyen@example.com")
                        .address("789 Oak Street, Da Nang")
                        .createAt(LocalDate.of(2024, 6, 15))
                        .enable(false)
                        .url("https://example.com/avatar/an.png")
                        .deleteAt(LocalDateTime.of(2024, 8, 1, 14, 30))
                        .image("an.png")
                        .build()
        );
    }

    @Test
    void createUser_success() throws Exception {
        Mockito.when(usersInterface.createUser(any(UsersRequest.class)))
                .thenReturn(usersResponse);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(usersRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.firstName").value("John"));
    }

    @Test
    void updateUser_success() throws Exception {
        Mockito.when(usersInterface.updateUser(any(UsersUpdateRequest.class)))
                .thenReturn(usersResponse);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(usersUpdateRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.firstName").value("John"));
    }

    @Test
    void deleteUser_success() throws Exception {
        // Mock void method
        Mockito.doNothing().when(usersInterface).deleteUser(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }

    @Test
    void getAllUsers_success() throws Exception {
        // Mock Page
        org.springframework.data.domain.Page<UsersResponse> page =
                new org.springframework.data.domain.PageImpl<>(usersResponseList);

        Mockito.when(usersInterface.getAllUsers(0, 4)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("page", "0")
                        .param("size", "4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[1].firstName").value("An"));
    }


}
