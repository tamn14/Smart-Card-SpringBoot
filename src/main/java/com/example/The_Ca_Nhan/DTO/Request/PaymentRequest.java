package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.PaymentMethod;
import com.example.The_Ca_Nhan.Properties.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @Enumerated(EnumType.STRING)
    private PaymentMethod method ;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status ;

    private LocalDate payDate ;
}
