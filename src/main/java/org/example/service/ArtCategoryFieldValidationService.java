package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.field.*;
import org.example.json.ArtCategoryRequest;
import org.example.json.ValidationResponse;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArtCategoryFieldValidationService extends BaseFieldValidationService {


    public ValidationResponse artCategoryFieldValidation(ArtCategoryRequest artCategoryRequest, String traceId) {
        log.info("artCategoryFieldValidation started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        addErrorToList(Name.isValid(artCategoryRequest.getName()), errorList);
        addErrorToList(Description.isValid(artCategoryRequest.getDescription()), errorList);

        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("artCategoryFieldValidation started, traceId: {}", traceId);
        return validationResponse;
    }

    public ValidationResponse updateArtCategoryFieldValidation(ArtCategoryRequest artCategoryRequest, String traceId) {
        log.info("updateArtCategoryFieldValidation started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        if(artCategoryRequest.getName() != null) addErrorToList(Name.isValid(artCategoryRequest.getName()), errorList);
        if(artCategoryRequest.getDescription() != null) addErrorToList(Description.isValid(artCategoryRequest.getDescription()), errorList);

        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("updateArtCategoryFieldValidation started, traceId: {}", traceId);
        return validationResponse;
    }
}
