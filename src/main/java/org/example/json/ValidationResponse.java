package org.example.json;

import lombok.Data;

@Data
public class ValidationResponse {

    private ArrayValidationErrors validationErrors;

    public ValidationResponse() {
        this.validationErrors = new ArrayValidationErrors();
    }
}
