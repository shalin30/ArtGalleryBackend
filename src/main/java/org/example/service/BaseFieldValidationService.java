package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.json.ArrayValidationErrors;
import org.example.json.ValidationError;
import org.example.json.ValidationResponse;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

@Slf4j
@Service
public class BaseFieldValidationService {

    ResourceBundle errorMessages;

    public BaseFieldValidationService() {
        errorMessages = this.loadValidationMessageBundle();
    }

    protected ValidationResponse prepareValidationMessage(List<String> errorList) {
        ValidationResponse response = new ValidationResponse();
        ArrayValidationErrors validationErrors = new ArrayValidationErrors();
        response.setValidationErrors(validationErrors);

        if (errorList != null && !errorList.isEmpty()) {
            for (String errorFieldCode : errorList) {
                try {
                    ValidationError error = new ValidationError();
                    error.setTextID(errorFieldCode);
                    error.setText(errorMessages.getString(errorFieldCode));
                    validationErrors.getErrorList().add(error);
                } catch (Exception e) {
                    ValidationError error = new ValidationError();
                    error.setTextID(errorFieldCode);
                    error.setText("Validation Message not found");
                    validationErrors.getErrorList().add(error);
                }
            }
        }
        return response;
    }

    protected void addErrorToList(String errorCode, List<String> errorList) {
        if (errorCode != null && !errorCode.trim().isEmpty()) {
            errorList.add(errorCode);
        }
    }

    private ResourceBundle loadValidationMessageBundle() {
        ResourceBundle bundle = null;
        try {
            bundle = new PropertyResourceBundle(Files.newInputStream(Paths.get("src/main/resources/ValidationMessages.properties")));
        } catch (Throwable th) {
            log.error("Exception in loadValidationMessagesBundle method : {}", th.getMessage(), th);
        }
        return bundle;
    }

    protected boolean anyValidationErrorExists(ValidationResponse validationResponse) {
        return (validationResponse == null) || (validationResponse.getValidationErrors() != null && validationResponse.getValidationErrors().getErrorList() != null && !validationResponse.getValidationErrors().getErrorList().isEmpty());
    }
}
