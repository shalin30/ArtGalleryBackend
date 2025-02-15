package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.example.mapper.ArtCategoryResponseMapper;
import org.example.repository.ArtCategoryRepository;
import org.example.repository.ArtPieceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArtCategoryOrchestratorService extends BaseFieldValidationService{

    @Autowired
    ArtCategoryRepository artCategoryRepository;

    @Autowired
    ArtPieceRepository artPieceRepository;

    @Autowired
    ArtCategoryFieldValidationService artCategoryFieldValidationService;

    @Autowired
    ArtCategoryResponseMapper mapper;


    public ArtCategoryOrchestratorService(ArtCategoryRepository artCategoryRepository, ArtCategoryFieldValidationService artCategoryFieldValidationService,
                                          ArtCategoryResponseMapper mapper, ArtPieceRepository artPieceRepository) {
        this.artCategoryRepository = artCategoryRepository;
        this.artCategoryFieldValidationService = artCategoryFieldValidationService;
        this.mapper = mapper;
        this.artPieceRepository = artPieceRepository;
    }

    public ArtCategoryResponse insertArtCategory(ArtCategoryRequest request, String traceId){
        log.info("insertArtCategory started, traceId: {}", traceId);
        ArtCategoryResponse response;
        ValidationResponse validationResponse = artCategoryFieldValidationService.artCategoryFieldValidation(request, traceId);

        if(!anyValidationErrorExists(validationResponse)) {
            Categories categories = new Categories();
            categories.setName(request.getName());
            categories.setDescription(request.getDescription());
            categories.setCreatedAt(LocalDateTime.now());
            categories.setModifiedAt(LocalDateTime.now());
            artCategoryRepository.save(categories);

            response = mapper.mapSuccessResponse(categories,traceId);
        } else {
            response = mapper.mapValidationErrorResponse(validationResponse,traceId);
        }
        log.info("insertArtCategory ended, traceId: {}", traceId);
        return response;
    }

    public ArtCategoryResponse updateArtCategory(ArtCategoryRequest request, String traceId){
        log.info("updateArtCategory started, traceId: {}", traceId);
        ArtCategoryResponse response;

        ValidationResponse validationResponse = artCategoryFieldValidationService.updateArtCategoryFieldValidation(request, traceId);

        if(!anyValidationErrorExists(validationResponse)) {

            Categories categories = artCategoryRepository.findByCategoryId(Integer.valueOf(request.getCategoryId()));

            if(categories != null) {
                if(request.getName() != null) categories.setName(request.getName());
                if(request.getDescription() != null) categories.setDescription(request.getDescription());
                categories.setModifiedAt(LocalDateTime.now());
                artCategoryRepository.save(categories);
                response = mapper.mapUpdateSuccessResponse(categories,traceId);
            } else {
                response = mapper.mapNoCategoryFoundResponse(traceId);
            }
        } else {
            response = mapper.mapValidationErrorResponse(validationResponse,traceId);
        }
        log.info("updateArtCategory ended, traceId: {}", traceId);
        return response;
    }

    public List<ArtCategoryResponse> getArtCategories(String traceId){
        log.info("getArtCategories started, traceId: {}", traceId);

        List<ArtCategoryResponse> responseList = new ArrayList<>();
        ArtCategoryResponse response = new ArtCategoryResponse();

        List<Categories> categories = artCategoryRepository.findAll();

        if(!CollectionUtils.isEmpty(categories)) {
            log.info("getArtCategories ended, traceId: {}", traceId);
            return categories.stream().map(this::convertToDTO).collect(Collectors.toList());

        } else {
            response.setStatus(HttpStatus.NOT_FOUND);
            responseList.add(response);
        }
        log.info("getArtCategories ended, traceId: {}", traceId);
        return responseList;
    }

    public ArtCategoryResponse deleteArtCategory(String categoryId, String traceId) {
        log.info("deleteArtCategory started, traceId: {}", traceId);
        ArtCategoryResponse artCategoryResponse;
        Categories categories = artCategoryRepository.findByCategoryId(Integer.valueOf(categoryId));
        if(categories != null) {
            List<ArtPiece> artPiece = artPieceRepository.findArtPiecesByCategoryId(Integer.valueOf(categoryId));
            if (artPiece != null && !artPiece.isEmpty()) {
                artCategoryResponse = mapper.mapDeleteFailedResponse(traceId);
            } else {
                artCategoryRepository.deleteById(Integer.valueOf(categoryId));
                artCategoryResponse = mapper.mapDeleteSuccessResponse(traceId);
            }
        } else {
            artCategoryResponse = mapper.mapNoCategoryFoundResponse(traceId);
        }
        log.info("deleteArtCategory ended, traceId: {}", traceId);
        return artCategoryResponse;
    }

    private ArtCategoryResponse convertToDTO(Categories categories){
        ArtCategoryResponse response =  new ArtCategoryResponse();
        response.setCategoryId(categories.getCategoryId().toString());
        response.setName(categories.getName());
        response.setDescription(categories.getDescription());
        return response;
    }
}
