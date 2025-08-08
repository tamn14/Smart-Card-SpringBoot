package com.example.The_Ca_Nhan.Exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "user not existed", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    CARD_NOT_FOUND(1005,"Card not existed" ,HttpStatus.NOT_FOUND ),
    ORDERS_NOT_FOUND(1014,"orders not existed" ,HttpStatus.NOT_FOUND ),
    PAYMENT_NOT_FOUND(1015,"payment not existed" ,HttpStatus.NOT_FOUND ),
    EDUCATION_NOT_FOUND(1008,"education not existed" ,HttpStatus.NOT_FOUND ),
    SKILL_NOT_FOUND(1018,"skill not existed" ,HttpStatus.NOT_FOUND ),
    CANNOT_CREATE_QR(1019,"cannot create QR ",HttpStatus.BAD_REQUEST ),
    PROJECT_NOT_FOUND(1017,"project not existed" ,HttpStatus.NOT_FOUND ),
    PROFILES_NOT_FOUND(1016,"profiles not existed" ,HttpStatus.NOT_FOUND ),
    EXPERIENCES_NOT_FOUND(1014,"experiences not existed" ,HttpStatus.NOT_FOUND ),
    MEDIA_NOT_FOUND(1013,"media not existed" ,HttpStatus.NOT_FOUND ),
    ACCOUNTS_BLOCK(1006,"Account has been block" ,HttpStatus.UNAUTHORIZED ),
    USER_EXISTED(1007, "User already existed", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1009, "Email already existed", HttpStatus.CONFLICT),
    S3_SERVICE_FAILED(1010, "Failed to upload file in AWS ", HttpStatus.BAD_REQUEST),
    KEYCLOAK_SERVICE_FAILED(1020, "Can not get token from Keycloak ", HttpStatus.BAD_REQUEST),
    EMAIL_SERVICE_FAILED(1021, "Can not send mail ", HttpStatus.BAD_REQUEST),
    FILE_IMAGE_EMPTY(1011, "File image is emty ", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(1024, "Invalid request , can not find request ", HttpStatus.BAD_REQUEST),
    USER_ALREADY_DELETED(1022, "User already deleted ", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1012, "Username is missing", HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
