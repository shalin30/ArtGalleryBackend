package org.example.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;
import javax.mail.internet.MimeMessage;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmailWithSuccess() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        emailService.sendEmail("test@example.com", "Test Subject", "Test Body", true);
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendEmailWithException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            emailService.sendEmail("123", "Test Subject", "Test Body", true);
        });
        assertTrue(exception.getMessage().contains("Email sending failed"));
    }
}