package org.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    EmailService service;

    @Test
    public void testSendEmail(){
        String to = "recipient";
        String subject = "test subject";
        String body = "test body";
        Mockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        service.sendEmail(to,subject,body);
        verify(mailSender,times(1)).send(any(SimpleMailMessage.class));
    }
}