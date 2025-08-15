package com.example.The_Ca_Nhan.Controller.UnitTest;


import com.example.The_Ca_Nhan.Controller.OrdersController;
import com.example.The_Ca_Nhan.DTO.Request.OrdersRequest;
import com.example.The_Ca_Nhan.DTO.Request.PaymentRequest;
import com.example.The_Ca_Nhan.DTO.Response.OrdersResponse;
import com.example.The_Ca_Nhan.DTO.Response.PaymentResponse;
import com.example.The_Ca_Nhan.Properties.OrderType;
import com.example.The_Ca_Nhan.Properties.OrdersStatus;
import com.example.The_Ca_Nhan.Properties.PaymentMethod;
import com.example.The_Ca_Nhan.Properties.PaymentStatus;
import com.example.The_Ca_Nhan.Service.Interface.OrdersInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(OrdersController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc ;
    @MockBean
    private OrdersInterface ordersInterface ;


    private OrdersRequest ordersRequest ;
    private OrdersResponse ordersResponse ;
    private List<OrdersResponse> ordersResponseList ;
    private int orderId = 1;

    @BeforeEach
    void init() {
        // --- OrdersRequest cho create ---
        ordersRequest = OrdersRequest.builder()
                .orderType(OrderType.NEW_CARD)
                .totalAmount(500)
                .status(OrdersStatus.PENDING)
                .address("Can Tho")
                .ordersDate(LocalDate.now())
                .paymentRequest(PaymentRequest.builder()
                        .method(PaymentMethod.CASH)
                        .status(PaymentStatus.PENDING)
                        .build())
                .cardId(1)
                .build();

        // --- OrdersResponse cho create
        ordersResponse = OrdersResponse.builder()
                .orderId(1)
                .orderType(OrderType.NEW_CARD)
                .totalAmount(500)
                .status(OrdersStatus.PENDING)
                .address("123 Nguyen Trai, HN")
                .ordersDate(LocalDate.now())
                .paymentResponse(PaymentResponse.builder()
                        .method(PaymentMethod.CASH)
                        .status(PaymentStatus.PENDING)
                        .build())
                .build();

        // --- List OrdersResponse cho findAll ---
        ordersResponseList = List.of(
                ordersResponse,
                OrdersResponse.builder()
                        .orderId(2)
                        .orderType(OrderType.RENEWAL)
                        .totalAmount(1000)
                        .status(OrdersStatus.PENDING)
                        .address("Can Tho ")
                        .ordersDate(LocalDate.now().minusDays(1))
                        .paymentResponse(PaymentResponse.builder()
                                .method(PaymentMethod.CASH)
                                .status(PaymentStatus.PENDING)
                                .build())
                        .build()
        );
    }

    @Test
    void createOrder_success() throws Exception {
        Mockito.when(ordersInterface.insertOrders(any(OrdersRequest.class)))
                .thenReturn(ordersResponse);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String content = mapper.writeValueAsString(ordersRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }


    @Test
    void deleteOrder_success() throws Exception {
        Mockito.doNothing().when(ordersInterface).deleteOrders(orderId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"));
    }

    @Test
    void findAllByUser_success() throws Exception {
        Mockito.when(ordersInterface.findAllByUser()).thenReturn(ordersResponseList);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[0].orderId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result[1].orderId").value(2));
    }

    @Test
    void findOrderById_success() throws Exception {
        Mockito.when(ordersInterface.findById(orderId)).thenReturn(ordersResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.orderId").value(orderId));
    }

    @Test
    void findAllToAdmin_success() throws Exception {
        Page<OrdersResponse> page = new PageImpl<>(ordersResponseList);
        Mockito.when(ordersInterface.findAllToAdmin(0, 10)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mess").value("Success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.content[0].orderId").value(1));
    }

    @Test
    void qrImage_success() throws Exception {
        byte[] fakeQr = new byte[]{1, 2, 3};
        Mockito.when(ordersInterface.QrForPayment(orderId)).thenReturn(fakeQr);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/qr/image/{order}", orderId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }








}
