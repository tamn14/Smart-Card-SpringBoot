package com.example.The_Ca_Nhan.Security;

import com.example.The_Ca_Nhan.Entity.Users;
import com.example.The_Ca_Nhan.Exception.AppException;
import com.example.The_Ca_Nhan.Exception.ErrorCode;
import com.example.The_Ca_Nhan.Repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

//--CLASS NAY DUNG DE KIEM TRA NGUOI DUNG HIEN TAI CO PHAI LA CHU SO HUU CUA RESOURCE NAO KHONG--//
@Component("authz")
public class AuthorizationHelper {
    @Autowired
    private UsersRepository usersRepo ;
    public boolean isOwner (int  userId) {
        // Lay authentication hien tai tu security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
        if(authentication == null) {
            return false ; // chua ai dang nhap nen tra ve false
        }
        // lay sub keycloak
        var principal = authentication.getPrincipal() ; // lay principal
        if(principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal ;
            // lay sub
            String sub = jwt.getClaimAsString("sub");
            // so sanh  userKeycloakId
            Users user = usersRepo.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
            return sub.equals(user.getKeycloakId());
        }
        return false ;
    }
}
