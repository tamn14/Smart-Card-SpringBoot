package com.example.The_Ca_Nhan.Controller.UnitTest;


import com.example.The_Ca_Nhan.Controller.CardController;
import com.example.The_Ca_Nhan.DTO.Request.CardRequest;
import com.example.The_Ca_Nhan.DTO.Response.CardResponse;
import com.example.The_Ca_Nhan.Service.Interface.CardInterface;
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

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private CardInterface cardInterface ;

    private CardRequest cardRequest ;
    private CardResponse createCardResponse  ;
    private CardResponse updateCardResponse   ;
    private List<CardResponse> cardResponseList ;
    private int cardID = 1 ;

    @BeforeEach
    void init() {
        cardRequest = CardRequest.builder()
                .name("CardTest")
                .description("Mo ta test")
                .price(100)
                .build();

        createCardResponse = CardResponse.builder()
                .cardId(1)
                .name("CardTest")
                .description("Mo ta test")
                .price(100)
                .build();

        // Response cho update
        updateCardResponse = CardResponse.builder()
                .cardId(1)
                .name("CardUpdate")
                .description("Mo ta test update")
                .price(1020)
                .build();

        cardResponseList = List.of(
                createCardResponse ,
                CardResponse.builder()
                        .cardId(2)
                        .name("CardTest2")
                        .description("Mo ta test2")
                        .price(1005)
                        .build()

        ) ;

    }


    @Test
    void createCard_success() throws Exception {

        Mockito.when(cardInterface.insertCard(any(CardRequest.class)))
                .thenReturn(createCardResponse) ;

        ObjectMapper mapper = new ObjectMapper() ;
        String content = mapper.writeValueAsString(cardRequest) ;
        mockMvc.perform(MockMvcRequestBuilders.multipart("/cards")
                        .param("name", cardRequest.getName())
                        .param("description", cardRequest.getDescription())
                        .param("price", String.valueOf(cardRequest.getPrice())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("CardTest"));
    }

    @Test
    void updateCard_success() throws Exception {
        Mockito.when(cardInterface.updateCard(any(CardRequest.class), eq(cardID)))
                .thenReturn(updateCardResponse);

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(cardRequest);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/cards/{id}", cardID)
                        .file("file", new byte[0])
                        .param("name", cardRequest.getName())
                        .param("description", cardRequest.getDescription())
                        .param("price", String.valueOf(cardRequest.getPrice()))
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("CardUpdate"));
    }

    @Test
    void deleteCard_success() throws Exception {
        // Mock void method
        Mockito.doNothing().when(cardInterface).deleteCard(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cards/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }





    @Test
    void findById_success() throws Exception {
        Mockito.when(cardInterface.findById(cardID)).thenReturn(createCardResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/id/{id}", cardID))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("CardTest"));
    }

    @Test
    void findByName_success() throws Exception {
        org.springframework.data.domain.Page<CardResponse> page =
                new org.springframework.data.domain.PageImpl<>(cardResponseList);

        Mockito.when(cardInterface.findByName("CardTest", 0, 10)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/name/{name}", "CardTest")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].name").value("CardTest"));
    }

    @Test
    void findByName_emptyResult() throws Exception {
        org.springframework.data.domain.Page<CardResponse> emptyPage =
                new org.springframework.data.domain.PageImpl<>(List.of());

        Mockito.when(cardInterface.findByName("NotFound", 0, 10)).thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/name/{name}", "NotFound")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content").isEmpty());
    }

    @Test
    void getAllCard_success() throws Exception {
        org.springframework.data.domain.Page<CardResponse> page =
                new org.springframework.data.domain.PageImpl<>(cardResponseList);

        Mockito.when(cardInterface.findAll(0, 10)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].name").value("CardTest"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[1].name").value("CardTest2"));
    }





}
