package org.example.mapper;

import org.example.json.PasswordResetResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class PasswordResetMapperTest {

    @InjectMocks
    PasswordResetMapper mapper;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapNoUserFoundResponse(){
        PasswordResetResponse response = mapper.mapNoUserFoundResponse(traceId);
        Assertions.assertEquals("No user found", response.getMessage());
    }

    @Test
    public void testMapSuccessResponse(){
        PasswordResetResponse response = mapper.mapSuccessResponse(traceId);
        Assertions.assertEquals("mail send successfully..!!", response.getMessage());
    }

    @Test
    public void testMapNoResetTokenFoundResponse(){
        PasswordResetResponse response = mapper.mapNoResetTokenFoundResponse(traceId);
        Assertions.assertEquals("No reset token found", response.getMessage());
    }

    @Test
    public void testMapTokenExpiredResponse(){
        PasswordResetResponse response = mapper.mapTokenExpiredResponse(traceId);
        Assertions.assertEquals("Token expired", response.getMessage());
    }

    @Test
    public void testMapSuccessPasswordResetResponse(){
        PasswordResetResponse response = mapper.mapSuccessPasswordResetResponse(traceId);
        Assertions.assertEquals("Password updated successfully..!!", response.getMessage());
    }
}