package com.example.The_Ca_Nhan.Config;

// ------------- file nay dung de tao ngay mot tai khoan admin khi vua khoi dong --------------------//


import com.example.The_Ca_Nhan.DTO.KeycloakRequest.Credential;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.UserCreationParam;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Properties.RoleTemplate;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.KeycloakClientTokenService;
import com.example.The_Ca_Nhan.Util.Extract;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {
    @Bean
    ApplicationRunner applicationRunner(UsersRepository usersRepo,
                                        IdentityProviderRepo identityProviderRepo,
                                        KeycloakClientTokenService keycloakClientTokenService,
                                        IdpProperties idpProperties,
                                        Extract extract
                                       ) {
        return args -> {
            if (!usersRepo.existsByUserName("admin")) {

                // lay access Token de co the goi API keycloak tao nguoi dung
                var accessToken = keycloakClientTokenService.getAccessToken();
                var creationResponse = identityProviderRepo.createUser(
                        idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        UserCreationParam.builder()
                                .username("admin")
                                .enabled(true)
                                .emailVerified(false)
                                .credentials(List.of(Credential.builder()
                                        .type("password")
                                        .value("admin")
                                        .temporary(false)
                                        .build()))
                                .build());
                String userKeycloakId = extract.extractUserId(creationResponse);

                // gan role cho user vua tao
                var role = identityProviderRepo.getRealmRoleByName(idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        RoleTemplate.ADMIN.getValue()
                );
                identityProviderRepo.assignRealmRolesToUser(
                        idpProperties.getRealm(),
                        "Bearer " + accessToken,
                        userKeycloakId,
                        List.of(role)
                );

                // luu thong tin vao db
                Users users = Users.builder()
                        .userName("admin")
                        .email("Admin@gmail.com")
                        .keycloakId(userKeycloakId)
                        .enable(true)
                        .build();
                // luu user vao db ( vi day la he thong booking nen co the hieu khi tao user moi la tao passenger moi)
                usersRepo.save(users);
                
            }

        };
    }
}