package com.example.The_Ca_Nhan.Service.Implemment;

import com.example.The_Ca_Nhan.DTO.KeycloakRequest.GoogleTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.RevokeUserParam;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.UserRefreshTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.UserAccessTokenExchangeParam;
import com.example.The_Ca_Nhan.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Request.RefreshRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Model.AuthModelTokenInfor;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Interface.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class KeycloakUserTokenServiceImpl implements AuthenticationService {
    private final UsersRepository usersRepo;
    private final IdentityProviderRepo identityProviderRepo;
    private final IdpProperties idpProperties;
    private final TokenBlackListServiceImpl tokenBlacklistService;


    private void validateActiveUser(String username) {
        var user = usersRepo.findByUserName(username);
        if (user == null || !user.isEnable() || user.getDeleteAt() != null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

    }

    private AuthModelTokenInfor requestNewToken(LoginRequest loginRequest) {
        // kiem tra neu user da bi xoa trong db (xoa mem) thi khong duoc login
        validateActiveUser(loginRequest.getUserName());
        // tao tokenInfo
        UserAccessTokenExchangeParam param = UserAccessTokenExchangeParam.builder()
                .grant_type("password")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .username(loginRequest.getUserName())
                .password(loginRequest.getPassword())
                .scope("openid offline_access")
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserAccessToken(
                idpProperties.getRealm(),
                param
        );
        return new AuthModelTokenInfor(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        AuthModelTokenInfor tokenInfo = requestNewToken(loginRequest);

        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getAccessTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();


    }

    @Override
    public String getAccessToken(LoginRequest loginRequest) {

        return requestNewToken(loginRequest).getAccessToken();
    }


    @Override
    public AuthenticationResponse refreshToken(RefreshRequest refreshRequest) {
        String refreshToken = refreshRequest.getToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        UserRefreshTokenExchangeParam param = UserRefreshTokenExchangeParam.builder()
                .grant_type("refresh_token")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();

        UserTokenExchangeResponse response = identityProviderRepo.exchangeUserRefreshToken(
                idpProperties.getRealm(),
                param
        );
        AuthModelTokenInfor tokenInfo = new AuthModelTokenInfor(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );
        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getRefreshTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();
    }


    @Override
    public void logout(LogoutRequest logoutRequest) {
        String refreshToken = logoutRequest.getRefreshToken();
        String accessToken = logoutRequest.getAccessToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        RevokeUserParam param = RevokeUserParam.builder()
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .refresh_token(refreshToken)
                .build();


        identityProviderRepo.revokeUserToken(
                idpProperties.getRealm(),
                param
        );
        if (accessToken != null && !accessToken.isEmpty()) {
            tokenBlacklistService.addToBlacklist(accessToken);

        }
    }

    @Override
    public AuthenticationResponse googleLogin(String code) {
        if (code == null || code.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        GoogleTokenExchangeParam tokenExchangeParam = GoogleTokenExchangeParam.builder()
                .grant_type("authorization_code")
                .client_id(idpProperties.getClientId())
                .client_secret(idpProperties.getClientSecret())
                .code(code)
                .redirect_uri(idpProperties.getGoogleRedirectUri())
                .scope("openid offline_access")
                .build() ;

        UserTokenExchangeResponse response = identityProviderRepo.exchangeGoogleCodeToken(
                idpProperties.getRealm(),
                tokenExchangeParam
        );

        AuthModelTokenInfor tokenInfo = new AuthModelTokenInfor(
                response.getAccessToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn())),
                response.getRefreshToken(),
                Instant.now().plusSeconds(Long.parseLong(response.getRefreshExpiresIn()))
        );

        return AuthenticationResponse.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(tokenInfo.getAccessTokenExpiry().getEpochSecond() - Instant.now().getEpochSecond())
                .authenticated(true)
                .build();
    }
}
