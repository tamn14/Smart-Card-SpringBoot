package com.example.The_Ca_Nhan.Service.Implemment;


import com.example.The_Ca_Nhan.DTO.KeycloakRequest.ClientTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakResponse.ClientTokenExchangeResponse;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Service.Interface.KeycloakClientTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class KeycloakClientTokenServiceImpl implements KeycloakClientTokenService {
    private final IdpProperties idpProperties;
    private final IdentityProviderRepo identityProviderRepo;

    private String cachedToken; // luu lai accessToken da lay duoc tu keycloak
    private Instant tokenExpiry; // thoi gian het han cua token, muc dich nhan biet token da het han chua

    @Override
    public synchronized String getAccessToken() {
        // kiem tra neu token chua co hoac thoi gian het han con 60 giay nua se toi => refreshToken
        if (cachedToken == null || tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(60))) {
            refreshToken();
        }
        return cachedToken;
    }


    @Override
    public void refreshToken() {
        ClientTokenExchangeParam clientTokentExchangeParam = ClientTokenExchangeParam.builder()
                .grant_type("client_credentials") // gia tri mac dinh cua keycloak
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .scope("openid")
                .build();
        ClientTokenExchangeResponse response = identityProviderRepo.exchangeClientToken(
                idpProperties.getRealm(),
                clientTokentExchangeParam
                );
        if (response == null || response.getAccessToken() == null) {
            throw new AppException(ErrorCode.KEYCLOAK_SERVICE_FAILED);
        }

        this.cachedToken = response.getAccessToken();
        this.tokenExpiry = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn()));
        ;
    }
}
