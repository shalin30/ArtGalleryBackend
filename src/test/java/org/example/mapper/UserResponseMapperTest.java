package org.example.mapper;

import org.example.entity.UserDetails;
import org.example.json.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserResponseMapperTest {

    @InjectMocks
    UserResponseMapper mapper;

    UserDetails userDetails;

    String traceId = UUID.randomUUID().toString();

    @Test
    public void testMapSuccessResponse(){
        UserCreationResponse response = mapper.mapSuccessResponse(getUserDetails(), traceId);
        Assertions.assertEquals("User created successfully..!!", response.getMessage());
    }

    @Test
    public void testMapUpdateSuccessResponse(){
        UserCreationResponse response = mapper.mapUpdateSuccessResponse(getUserDetails(), traceId);
        Assertions.assertEquals("User updated successfully..!!", response.getMessage());
    }

    @Test
    public void testMapNoUserFoundResponse(){
        UserCreationResponse response = mapper.mapNoUserFoundResponse(traceId);
        Assertions.assertEquals("No user found", response.getMessage());
    }

    @Test
    public void testMapDuplicateEmailResponse(){
        UserCreationResponse response = mapper.mapDuplicateEmailResponse(traceId);
        Assertions.assertEquals("Email already exists", response.getMessage());
    }

    @Test
    public void testMapDuplicateUserNameResponse(){
        UserCreationResponse response = mapper.mapDuplicateUserNameResponse(traceId);
        Assertions.assertEquals("Username already exists", response.getMessage());
    }

    @Test
    public void testMapDeActivateUserResponse(){
        UserCreationResponse response = mapper.mapDeActivateUserResponse(traceId);
        Assertions.assertEquals("User deleted", response.getMessage());
    }

    @Test
    public void testMapValidationErrorResponse(){
        ValidationResponse validationResponse = new ValidationResponse();
        ArrayValidationErrors arrayValidationErrors = new ArrayValidationErrors();
        List<ValidationError> validationErrors = new ArrayList<>();
        ValidationError validationError = new ValidationError();
        validationError.setTextID("error");
        validationError.setText("test error");
        validationErrors.add(validationError);
        arrayValidationErrors.setErrorList(validationErrors);
        validationResponse.setValidationErrors(arrayValidationErrors);
        UserCreationResponse response = mapper.mapValidationErrorResponse(validationResponse, traceId);
        Assertions.assertEquals("Field validation failed",response.getMessage());
    }

    @Test
    public void testMapLogOutFailureResponse(){
        LogoutResponse response = mapper.mapLogOutFailureResponse(traceId);
        Assertions.assertEquals("Invalid token", response.getMessage());
    }

    @Test
    public void testMapLogOutSuccessResponse(){
        LogoutResponse response = mapper.mapLogOutSuccessResponse(traceId);
        Assertions.assertEquals("Logout successful", response.getMessage());
    }

    public UserDetails getUserDetails(){
        userDetails = new UserDetails();
        userDetails.setUserId(1);
        userDetails.setUserName("test user");
        userDetails.setEmail("abc@gmail.com");
        return userDetails;
    }

}