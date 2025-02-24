package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.json.UserCreationRequest;
import org.example.json.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import org.example.field.*;

@Service
@Slf4j
public class UserFieldValidationService extends BaseFieldValidationService{

    public ValidationResponse userRegistrationFieldValidation(UserCreationRequest userCreationRequest, String traceId) {
        log.info("userRegistrationFieldValidation started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        addErrorToList(Name.isValid(userCreationRequest.getUserName()), errorList);
        addErrorToList(Email.isValid(userCreationRequest.getEmail()), errorList);
        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("userRegistrationFieldValidation started, traceId: {}", traceId);
        return validationResponse;
    }

    public ValidationResponse updateUserFieldValidation(UserCreationRequest userCreationRequest, String traceId) {
        log.info("update user field validation service started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        if(userCreationRequest.getUserName() != null) addErrorToList(Name.isValid(userCreationRequest.getUserName()), errorList);
        if(userCreationRequest.getEmail() != null) addErrorToList(Email.isValid(userCreationRequest.getEmail()), errorList);

        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("update user field validation service started, traceId: {}", traceId);
        return validationResponse;
    }
}
