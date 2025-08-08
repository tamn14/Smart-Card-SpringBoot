package com.example.The_Ca_Nhan.Model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthModelTokenInfor {
    String accessToken ; // luu thong tru accessToken cua user dang dang nhap
    Instant accessTokenExpiry ;  // luu tru thoi gian het han cua accessToken
    String refreshToken ; // luu thong tru refreshToken dung de refresh lai accessToken cua user dang dang nhap
    Instant refreshTokenExpiry ; //  // luu tru thoi gian het han cua refreshToken


}
