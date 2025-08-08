package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId ;
    private String summary  ;
    private String hobby;
    private String github ;
    private String facebook;
    private String career ;
    private String degree ;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users ;

}
