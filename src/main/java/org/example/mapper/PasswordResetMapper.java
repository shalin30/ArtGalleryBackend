package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.json.PasswordResetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PasswordResetMapper {

    public PasswordResetResponse mapNoUserFoundResponse(String traceId) {
        log.info("map no user found response started, traceId: {}", traceId);
        PasswordResetResponse response = new PasswordResetResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No user found");
        log.info("map no user found response ended, traceId: {}", traceId);
        return response;
    }

    public PasswordResetResponse mapSuccessResponse(String traceId) {
        log.info("map success response started, traceId: {}", traceId);
        PasswordResetResponse response = new PasswordResetResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("mail send successfully..!!");
        log.info("map success response ended, traceId: {}", traceId);
        return response;
    }

    public PasswordResetResponse mapNoResetTokenFoundResponse(String traceId) {
        log.info("map no reset token found response started, traceId: {}", traceId);
        PasswordResetResponse response = new PasswordResetResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("This link to reset password is either invalid or already used");
        log.info("map no reset token found response ended, traceId: {}", traceId);
        return response;
    }

    public PasswordResetResponse mapTokenExpiredResponse(String traceId) {
        log.info("map token expired response started, traceId: {}", traceId);
        PasswordResetResponse response = new PasswordResetResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setMessage("The link to reset password is expired");
        log.info("map token expired response ended, traceId: {}", traceId);
        return response;
    }

    public PasswordResetResponse mapSuccessPasswordResetResponse(String traceId) {
        log.info("map success password reset response started, traceId: {}", traceId);
        PasswordResetResponse response = new PasswordResetResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("Password updated successfully..!!");
        log.info("map success password reset response ended, traceId: {}", traceId);
        return response;
    }
}
