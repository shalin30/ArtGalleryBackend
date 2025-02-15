package org.example.json;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ArrayValidationErrors {
    List<ValidationError> errorList;

    public ArrayValidationErrors() {
        this.errorList = new ArrayList<>();
    }
}
