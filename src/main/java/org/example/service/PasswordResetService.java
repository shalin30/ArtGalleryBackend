package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.PasswordResetToken;
import org.example.entity.UserDetails;
import org.example.json.PasswordResetResponse;
import org.example.mapper.PasswordResetMapper;
import org.example.repository.PasswordResetTokenRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetMapper passwordResetMapper;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository, UserRepository userRepository,
                                EmailService emailService, PasswordResetMapper passwordResetMapper) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordResetMapper = passwordResetMapper;
    }

    public PasswordResetResponse sendPasswordResetLink(String email, String traceId) {
        log.info("sendPasswordResetLink started, traceId : {}", traceId);
        PasswordResetResponse response;
        UserDetails user = userRepository.findByEmail(email);

        if(user == null){
            log.info("sendPasswordResetLink ended, traceId : {}", traceId);
            return passwordResetMapper.mapNoUserFoundResponse(traceId);
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(user, token, new Date(System.currentTimeMillis() + 3600000));
        tokenRepository.save(resetToken);

        String resetLink = "http://localhost:8080/reset-password?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request", "Click here to reset your password: " + resetLink);
        response = passwordResetMapper.mapSuccessResponse(traceId);
        log.info("sendPasswordResetLink ended, traceId : {}", traceId);
        return response;
    }

    public PasswordResetResponse resetPassword(String token, String newPassword, String traceId) {
        log.info("resetPassword started, traceId : {}", traceId);
        PasswordResetResponse response;
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if(resetToken == null){
            log.info("resetPassword started, traceId : {}", traceId);
            return passwordResetMapper.mapNoResetTokenFoundResponse(traceId);
        }

        if (resetToken.getExpiryDate().before(new Date())) {
            log.info("resetPassword started, traceId : {}", traceId);
            return passwordResetMapper.mapTokenExpiredResponse(traceId);
        }

        UserDetails user = resetToken.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
        response = passwordResetMapper.mapSuccessPasswordResetResponse(traceId);
        log.info("resetPassword started, traceId : {}", traceId);
        return response;
    }
}
