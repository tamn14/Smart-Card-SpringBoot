package com.example.The_Ca_Nhan.Properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter @Getter
@Configuration
@ConfigurationProperties(prefix = "idp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IdpProperties {
    String url  ;
    String clientId ;
    String clientSecret ;
    String realm ;
    String GoogleRedirectUri ;
}
