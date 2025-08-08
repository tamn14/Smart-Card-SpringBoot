package com.example.The_Ca_Nhan.DTO.Request;

import com.example.The_Ca_Nhan.Properties.PaymentMethod;
import com.example.The_Ca_Nhan.Properties.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentUpdateRequest {
    @Enumerated(EnumType.STRING)
    private PaymentStatus status ;

}
