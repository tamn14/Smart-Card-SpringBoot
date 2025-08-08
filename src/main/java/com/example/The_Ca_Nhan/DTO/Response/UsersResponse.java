package com.example.The_Ca_Nhan.DTO.Response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
    private Integer id  ;
    private String keycloakId ;
    private String lastName ;
    private String firstName ;
    private String email ;
    private String address ;
    private LocalDate createAt ;
    private boolean enable;
    private String url ;
    private LocalDateTime deleteAt ;
    private String image ;

//    private List<MediaFileResponse> mediaFiles;

}
