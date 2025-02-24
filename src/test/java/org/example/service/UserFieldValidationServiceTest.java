package org.example.service;

import org.example.json.UserCreationRequest;
import org.example.json.ValidationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserFieldValidationServiceTest {

    @InjectMocks
    UserFieldValidationService userFieldValidationService;
    UserCreationRequest request;
    String traceId = UUID.randomUUID().toString();

    @Test
    public void testUserRegistrationFieldValidation(){
        request = new UserCreationRequest();
        request.setUserName("test name");
        request.setEmail("abc@gmail.com");

        ValidationResponse response = userFieldValidationService.userRegistrationFieldValidation(request, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, response.getValidationErrors().getErrorList());
    }

    @Test
    public void testUpdateUserRegistrationFieldValidation(){
        request = new UserCreationRequest();
        request.setUserName("test name");
        request.setEmail("abc@gmail.com");

        ValidationResponse response = userFieldValidationService.updateUserFieldValidation(request, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, response.getValidationErrors().getErrorList());
    }

    @Test
    public void testUpdateUserRegistrationFieldValidationWithNullValues(){
        request = new UserCreationRequest();
        request.setUserName(null);
        request.setEmail(null);

        ValidationResponse response = userFieldValidationService.updateUserFieldValidation(request, traceId);
        Assertions.assertEquals(Collections.EMPTY_LIST, response.getValidationErrors().getErrorList());
    }
}