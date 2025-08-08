package com.example.The_Ca_Nhan.DTO.Request;

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
public class OrdersUpdateRequest {

    private OrdersStatus status ;
    private PaymentUpdateRequest paymentUpdateRequest ;




}
