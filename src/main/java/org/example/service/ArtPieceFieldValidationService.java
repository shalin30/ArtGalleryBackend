package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.field.*;
import org.example.json.ArtPieceRequest;
import org.example.json.ValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ArtPieceFieldValidationService extends BaseFieldValidationService {

    public ValidationResponse artPieceFieldValidation(ArtPieceRequest artPieceRequest, String traceId) {
        log.info("ArtPieceFieldValidationService started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        addErrorToList(Title.isValid(artPieceRequest.getTitle()), errorList);
        addErrorToList(Description.isValid(artPieceRequest.getDescription()), errorList);
        addErrorToList(Price.isValid(String.valueOf(artPieceRequest.getPrice())), errorList);
        addErrorToList(ImageUrl.isValid(artPieceRequest.getImageUrl()), errorList);

        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("ArtPieceFieldValidationService started, traceId: {}", traceId);
        return validationResponse;
    }

    public ValidationResponse updateArtPieceFieldValidation(ArtPieceRequest artPieceRequest, String traceId) {
        log.info("updateArtPieceFieldValidation started, traceId: {}", traceId);

        List<String> errorList = new ArrayList<>();

        if(artPieceRequest.getTitle() != null) addErrorToList(Title.isValid(artPieceRequest.getTitle()), errorList);
        if(artPieceRequest.getDescription() != null) addErrorToList(Description.isValid(artPieceRequest.getDescription()), errorList);
        if(artPieceRequest.getPrice() != null) addErrorToList(Price.isValid(String.valueOf(artPieceRequest.getPrice())), errorList);
        if(artPieceRequest.getImageUrl() != null) addErrorToList(ImageUrl.isValid(artPieceRequest.getImageUrl()), errorList);

        ValidationResponse validationResponse = prepareValidationMessage(errorList);
        log.info("updateArtPieceFieldValidation started, traceId: {}", traceId);
        return validationResponse;
    }
}
