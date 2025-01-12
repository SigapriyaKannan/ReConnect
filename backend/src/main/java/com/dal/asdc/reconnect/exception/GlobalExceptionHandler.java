package com.dal.asdc.reconnect.exception;

import com.dal.asdc.reconnect.DTO.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleSecurityException(Exception exception) {
        Response<Void> response = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        exception.printStackTrace();
        if (exception instanceof BadCredentialsException) {
            response = new Response<>(401, "The username or password is incorrect", null);
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof AccountStatusException) {
            response = new Response<>(403, "The account is locked", null);
            status = HttpStatus.FORBIDDEN;
        } else if (exception instanceof AccessDeniedException) {
            response = new Response<>(401, "You are not authorized to access this resource", null);
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof SignatureException) {
            response = new Response<>(403, "The JWT signature is invalid", null);
            status = HttpStatus.FORBIDDEN;
        } else if (exception instanceof ExpiredJwtException) {
            response = new Response<>(403, "The JWT token has expired", null);
            status = HttpStatus.FORBIDDEN;
        }
        else{
            response = new Response<>(500, "Internal server error", null);
        }

        return new ResponseEntity<>(response, status);
    }
}