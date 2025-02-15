package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserDetails;
import org.example.json.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserResponseMapper {

    public UserCreationResponse mapSuccessResponse(UserDetails userDetails, String traceId) {
        log.info("map success response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setUserId(userDetails.getUserId().toString());
        response.setUserName(userDetails.getUserName());
        response.setEmail(userDetails.getEmail());
        response.setAddress1(userDetails.getAddress1());
        response.setAddress2(userDetails.getAddress2());
        response.setCity(userDetails.getCity());
        response.setState(userDetails.getState());
        response.setPostalCode(userDetails.getPostalCode());
        response.setStatus(HttpStatus.OK);
        response.setMessage("User created successfully..!!");
        log.info("map success response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapUpdateSuccessResponse(UserDetails userDetails, String traceId) {
        log.info("map update success response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setUserId(userDetails.getUserId().toString());
        response.setUserName(userDetails.getUserName());
        response.setEmail(userDetails.getEmail());
        response.setAddress1(userDetails.getAddress1());
        response.setAddress2(userDetails.getAddress2());
        response.setCity(userDetails.getCity());
        response.setState(userDetails.getState());
        response.setPostalCode(userDetails.getPostalCode());
        response.setStatus(HttpStatus.OK);
        response.setMessage("User updated successfully..!!");
        log.info("map update success response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapNoUserFoundResponse(String traceId) {
        log.info("map no user found response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No user found");
        log.info("map no user found response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapValidationErrorResponse(ValidationResponse validationResponse, String traceId) {
        log.info("map validation error response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setValidationResponse(validationResponse.getValidationErrors().getErrorList().toString());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Field validation failed");
        log.info("map validation error response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapDuplicateEmailResponse(String traceId) {
        log.info("map duplicate email response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.CONFLICT);
        response.setMessage("Email already exists");
        log.info("map duplicate email response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapDuplicateUserNameResponse(String traceId) {
        log.info("map duplicate username response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.CONFLICT);
        response.setMessage("Username already exists");
        log.info("map duplicate username response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapDeActivateUserResponse(String traceId) {
        log.info("map deactivate user response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("User deleted");
        log.info("map deactivate user response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapLogOutSuccessResponse(String traceId) {
        log.info("map logout success response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("Logout successful");
        log.info("map logout success response ended, traceId: {}", traceId);
        return response;
    }

    public UserCreationResponse mapLogOutFailureResponse(String traceId) {
        log.info("map logout failure response started, traceId: {}", traceId);
        UserCreationResponse response = new UserCreationResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setMessage("Invalid token");
        log.info("map logout failure response ended, traceId: {}", traceId);
        return response;
    }
}
