package org.example.service;

import org.example.entity.PasswordResetToken;
import org.example.entity.UserDetails;
import org.example.json.PasswordResetResponse;
import org.example.mapper.PasswordResetMapper;
import org.example.repository.PasswordResetTokenRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {
    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordResetMapper mapper;

    @InjectMocks
    PasswordResetService service;
    PasswordResetToken validToken;
    PasswordResetToken expiredToken;
    UserDetails user;
    String traceId = UUID.randomUUID().toString();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserDetails();
        user.setUserId(1);
        user.setEmail("test@example.com");

        validToken = new PasswordResetToken(user, "valid-token", new Date(System.currentTimeMillis() + 60000));
        expiredToken = new PasswordResetToken(user, "expired-token", new Date(System.currentTimeMillis() - 60000));
    }

    @Test
    public void testSendPasswordResetLink(){
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse();
        passwordResetResponse.setMessage("mail send successfully");

        UserDetails userDetails = new UserDetails();
        userDetails.setEmail("email");

        when(userRepository.findByEmail(anyString())).thenReturn(userDetails);
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(null);
        Mockito.doNothing().when(emailService).sendEmail(anyString(),anyString(), anyString());
        when(mapper.mapSuccessResponse(anyString())).thenReturn(passwordResetResponse);
        PasswordResetResponse response = service.sendPasswordResetLink("email", traceId);
        assertEquals("mail send successfully", response.getMessage());
    }

    @Test
    public void testSendPasswordResetLinkWithNullUser(){
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse();
        passwordResetResponse.setMessage("user not found");

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(mapper.mapNoUserFoundResponse(anyString())).thenReturn(passwordResetResponse);
        PasswordResetResponse response = service.sendPasswordResetLink("email", traceId);
        assertEquals("user not found", response.getMessage());
    }

    @Test
    public void testResetPasswordWithTokenNotFound() {
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse();
        passwordResetResponse.setMessage("Token not found");

        when(tokenRepository.findByToken("invalid-token")).thenReturn(null);
        when(mapper.mapNoResetTokenFoundResponse(traceId)).thenReturn(passwordResetResponse);

        PasswordResetResponse response = service.resetPassword("invalid-token", "newPass123", traceId);

        assertEquals("Token not found", response.getMessage());
        verify(tokenRepository, times(1)).findByToken("invalid-token");
    }

    @Test
    public void testResetPasswordWithTokenExpired() {
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse();
        passwordResetResponse.setMessage("Token expired");

        when(tokenRepository.findByToken("expired-token")).thenReturn(expiredToken);
        when(mapper.mapTokenExpiredResponse(traceId)).thenReturn(passwordResetResponse);

        PasswordResetResponse response = service.resetPassword("expired-token", "newPass123", traceId);

        assertEquals("Token expired", response.getMessage());
        verify(tokenRepository, times(1)).findByToken("expired-token");
    }

    @Test
    public void testResetPasswordSuccess() {
        PasswordResetResponse passwordResetResponse = new PasswordResetResponse();
        passwordResetResponse.setMessage("Password reset successful");

        when(tokenRepository.findByToken("valid-token")).thenReturn(validToken);
        when(mapper.mapSuccessPasswordResetResponse(traceId)).thenReturn(passwordResetResponse);

        PasswordResetResponse response = service.resetPassword("valid-token", "newPass123", traceId);

        assertEquals("Password reset successful", response.getMessage());
        verify(userRepository, times(1)).save(any(UserDetails.class));
        verify(tokenRepository, times(1)).delete(validToken);
    }
}