package com.example.The_Ca_Nhan.Entity;

import com.example.The_Ca_Nhan.Properties.MediaEntityType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MediaFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mediaId ;
    @Enumerated(EnumType.STRING)
    private MediaEntityType entityType;
    private String fileType ;
    private String fileName;
    private int entityId ;
    private String link ;
    private LocalDateTime updateDate ;
    @ManyToOne(
            fetch = FetchType.LAZY ,
            cascade = {
                    CascadeType.DETACH ,
                    CascadeType.PERSIST ,
                    CascadeType.MERGE ,
                    CascadeType.REFRESH
            }

    )
    @JoinColumn(name = "user_Id")
    private Users users ;

}
