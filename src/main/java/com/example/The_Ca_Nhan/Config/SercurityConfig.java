package com.example.The_Ca_Nhan.Config;


import com.example.The_Ca_Nhan.Security.TokenAuthenticationFilter;
import com.example.The_Ca_Nhan.Service.Interface.TokenBlackListInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SercurityConfig {

    private final String[] PUBLIC_ENDPOINTS_POST = {"/auth/login",
            "/auth/register",
            "/auth/logout" ,
            "/mail/customer",
            "/users",

           };

    private final String[] PUBLIC_ENDPOINTS_PATCH = {
            "/users/verify/**",

    };

    private final String[] PUBLIC_ENDPOINTS_GET = {
            "/cards",
            "/cards/id/**",
            "/cards/name/**" ,
            "/edu/public/**" ,
            "/exp/public/**" ,
            "/profile/public/**" ,
            "/project/public/**" ,
            "/skill/public/**" ,
            "/users/public/**" ,
            "/media/public/**",


    } ;




    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(TokenBlackListInterface tokenBlacklistService) {
        return new TokenAuthenticationFilter(tokenBlacklistService);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity , TokenAuthenticationFilter tokenAuthenticationFilter) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS_POST).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ENDPOINTS_GET).permitAll()
                        .requestMatchers(HttpMethod.PATCH, PUBLIC_ENDPOINTS_PATCH).permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenAuthenticationFilter, BasicAuthenticationFilter.class)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

//    @Bean
//    public SecurityFilterChain devFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .cors(cors -> {})
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll())
//                .csrf(AbstractHttpConfigurer::disable)
//                .build();
//    }



    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }
}
