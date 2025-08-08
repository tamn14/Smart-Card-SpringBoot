package com.example.The_Ca_Nhan.Entity;

import com.example.The_Ca_Nhan.Properties.PaymentMethod;
import com.example.The_Ca_Nhan.Properties.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payId ;
    @Enumerated(EnumType.STRING)
    private PaymentMethod method ;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status ;
    private LocalDate payDate ;
    @OneToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            },
            mappedBy = "payment"

    )

    private Orders orders ;
}
