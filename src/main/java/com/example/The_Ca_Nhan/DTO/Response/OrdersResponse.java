package com.example.The_Ca_Nhan.DTO.Response;

import com.example.The_Ca_Nhan.Properties.OrderType;
import com.example.The_Ca_Nhan.Properties.OrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    private int orderId ;
    @Enumerated(EnumType.STRING)
    private OrderType orderType ;
    private int totalAmount ;
    @Enumerated(EnumType.STRING)
    private OrdersStatus status ;
    private String address ;
    private LocalDate ordersDate ;

    private PaymentResponse paymentResponse ;
    private UsersResponse usersResponse ;
    private CardResponse cardResponse ;





}
