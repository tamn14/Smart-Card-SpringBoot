package com.example.The_Ca_Nhan.Entity;

import jakarta.persistence.*;
import lombok.*;
import com.example.The_Ca_Nhan.Entity.Users;



@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Educations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eduId ;
    private String schoolName ;
    private String degree ;
    private String startDate ;
    private String endDate ;
    private String description ;
    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "userId")
    private Users users ;


}
