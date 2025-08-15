package com.example.The_Ca_Nhan.Service.UnitTest;

import com.example.The_Ca_Nhan.DTO.Request.LoginRequest;
import com.example.The_Ca_Nhan.DTO.Request.LogoutRequest;
import com.example.The_Ca_Nhan.DTO.Request.RefreshRequest;
import com.example.The_Ca_Nhan.DTO.Response.AuthenticationResponse;
import com.example.The_Ca_Nhan.DTO.KeycloakRequest.*;
import com.example.The_Ca_Nhan.DTO.KeycloakResponse.UserTokenExchangeResponse;
import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Model.AuthModelTokenInfor;
import com.example.The_Ca_Nhan.Properties.IdpProperties;
import com.example.The_Ca_Nhan.Repository.IdentityProviderRepo;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import com.example.The_Ca_Nhan.Service.Implemment.KeycloakUserTokenServiceImpl;
import com.example.The_Ca_Nhan.Service.Implemment.TokenBlackListServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KeycloakUserTokenServiceImplTest {

    @InjectMocks
    private KeycloakUserTokenServiceImpl authService;

    @Mock
    private UsersRepository usersRepo;

    @Mock
    private IdentityProviderRepo identityProviderRepo;

    @Mock
    private IdpProperties idpProperties;

    @Mock
    private TokenBlackListServiceImpl tokenBlacklistService;

    private LoginRequest loginRequest;
    private UserTokenExchangeResponse tokenResponse;
    private Users user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        loginRequest = LoginRequest.builder()
                .userName("testuser")
                .password("password")
                .build();

        tokenResponse = UserTokenExchangeResponse.builder()
                .accessToken("access123")
                .refreshToken("refresh123")
                .expiresIn("3600")
                .refreshExpiresIn("7200")
                .build();

        user = new Users();
        user.setEnable(true);
        user.setDeleteAt(null);

        when(idpProperties.getClientId()).thenReturn("clientId");
        when(idpProperties.getClientSecret()).thenReturn("clientSecret");
        when(idpProperties.getRealm()).thenReturn("realm");
    }

    @Test
    void login_success() {
        when(usersRepo.findByUserName("testuser")).thenReturn(user);
        when(identityProviderRepo.exchangeUserAccessToken(anyString(), any(UserAccessTokenExchangeParam.class)))
                .thenReturn(tokenResponse);

        AuthenticationResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("access123", response.getAccessToken());
        assertEquals("refresh123", response.getRefreshToken());
        assertTrue(response.isAuthenticated());
    }

    @Test
    void login_failure_userNotExist() {
        when(usersRepo.findByUserName("testuser")).thenReturn(null);

        AppException ex = assertThrows(AppException.class, () -> authService.login(loginRequest));
        assertEquals(ErrorCode.USER_NOT_EXISTED, ex.getErrorCode());
    }

    @Test
    void refreshToken_success() {
        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setToken("refresh123");

        when(identityProviderRepo.exchangeUserRefreshToken(anyString(), any(UserRefreshTokenExchangeParam.class)))
                .thenReturn(tokenResponse);

        AuthenticationResponse response = authService.refreshToken(refreshRequest);

        assertNotNull(response);
        assertEquals("access123", response.getAccessToken());
        assertEquals("refresh123", response.getRefreshToken());
        assertTrue(response.isAuthenticated());
    }

    @Test
    void refreshToken_failure_emptyToken() {
        RefreshRequest refreshRequest = new RefreshRequest();
        refreshRequest.setToken(null);

        AppException ex = assertThrows(AppException.class, () -> authService.refreshToken(refreshRequest));
        assertEquals(ErrorCode.UNCATEGORIZED_EXCEPTION, ex.getErrorCode());
    }

    @Test
    void logout_success() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setAccessToken("access123");
        logoutRequest.setRefreshToken("refresh123");

        doNothing().when(identityProviderRepo).revokeUserToken(anyString(), any(RevokeUserParam.class));
        doNothing().when(tokenBlacklistService).addToBlacklist("access123");

        assertDoesNotThrow(() -> authService.logout(logoutRequest));

        verify(identityProviderRepo, times(1)).revokeUserToken(anyString(), any(RevokeUserParam.class));
        verify(tokenBlacklistService, times(1)).addToBlacklist("access123");
    }

    @Test
    void logout_failure_emptyRefreshToken() {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setRefreshToken(null);
        logoutRequest.setAccessToken("access123");

        AppException ex = assertThrows(AppException.class, () -> authService.logout(logoutRequest));
        assertEquals(ErrorCode.UNCATEGORIZED_EXCEPTION, ex.getErrorCode());
    }

    @Test
    void googleLogin_success() {
        String code = "google_code";

        when(identityProviderRepo.exchangeGoogleCodeToken(anyString(), any(GoogleTokenExchangeParam.class)))
                .thenReturn(tokenResponse);

        AuthenticationResponse response = authService.googleLogin(code);

        assertNotNull(response);
        assertEquals("access123", response.getAccessToken());
        assertEquals("refresh123", response.getRefreshToken());
        assertTrue(response.isAuthenticated());
    }

    @Test
    void googleLogin_failure_emptyCode() {
        AppException ex = assertThrows(AppException.class, () -> authService.googleLogin(""));
        assertEquals(ErrorCode.INVALID_REQUEST, ex.getErrorCode());
    }
}
