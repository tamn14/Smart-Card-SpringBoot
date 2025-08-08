package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.OrderType;
import com.example.The_Ca_Nhan.Properties.OrdersStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersRequest {
    @Enumerated(EnumType.STRING)
    private OrderType orderType ;
    private int totalAmount ;
    @Enumerated(EnumType.STRING)
    private OrdersStatus status ;
    private String address ;
    private LocalDate ordersDate ;

    private PaymentRequest paymentRequest ;
    private int cardId ;




}
