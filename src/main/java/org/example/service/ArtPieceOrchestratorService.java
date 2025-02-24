package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ArtPiece;
import org.example.entity.Categories;
import org.example.json.*;
import org.example.mapper.ArtPieceResponseMapper;
import org.example.repository.ArtCategoryRepository;
import org.example.repository.ArtPieceRepository;
import org.example.repository.OrderItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@Slf4j
public class ArtPieceOrchestratorService extends BaseFieldValidationService {

    @Autowired
    ArtPieceRepository artPieceRepository;

    @Autowired
    ArtCategoryRepository artCategoryRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    ArtPieceFieldValidationService artPieceFieldValidationService;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    ArtPieceResponseMapper mapper;

    public ArtPieceOrchestratorService(ArtPieceRepository artPieceRepository,
                                       ArtCategoryRepository artCategoryRepository,
                                       ArtPieceFieldValidationService artPieceFieldValidationService,
                                       ArtPieceResponseMapper mapper, OrderItemsRepository orderItemsRepository) {
        this.artPieceRepository = artPieceRepository;
        this.artCategoryRepository = artCategoryRepository;
        this.artPieceFieldValidationService = artPieceFieldValidationService;
        this.mapper = mapper;
        this.orderItemsRepository = orderItemsRepository;
    }

    public ArtPieceResponse insertArtPiece(ArtPieceRequest request, String traceId){
        log.info("insertArtPiece started, traceId: {}", traceId);
        ArtPieceResponse response;
        ValidationResponse validationResponse = artPieceFieldValidationService.artPieceFieldValidation(request, traceId);

        if(!anyValidationErrorExists(validationResponse)) {
            ArtPiece artPiece = new ArtPiece();
            artPiece.setTitle(request.getTitle());
            artPiece.setDescription(request.getDescription());
            artPiece.setPrice(request.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            artPiece.setImageUrl(null);
            artPiece.setArtist(request.getArtist());
            artPiece.setYear(Integer.valueOf(request.getYear()));
            artPiece.setDimensions(request.getDimensions());
            artPiece.setMedium(request.getMedium());
            artPiece.setCreatedAt(LocalDateTime.now());
            artPiece.setModifiedAt(LocalDateTime.now());

            Categories category = artCategoryRepository.findByCategoryId(Integer.valueOf(request.getCategoryId()));

            if (category != null) {
                artPiece.setCategory(category);
            } else {
                log.info("insertArtPiece ended, traceId: {}", traceId);
                return mapper.mapNoCategoryFoundResponse(traceId);
            }
            artPieceRepository.save(artPiece);
            response = mapper.mapSuccessResponse(artPiece,traceId);
        } else {
            response = mapper.mapValidationErrorResponse(validationResponse, traceId);
        }
        log.info("insertArtPiece ended, traceId: {}", traceId);
        return response;
    }

    public String uploadImageAndSetUrl(Integer artId, MultipartFile file) throws IOException {
        ArtPiece artPiece = artPieceRepository.findByArtId(artId);
        if (artPiece == null) {
            return null;
        }

        Map<String, Object> options = new HashMap<>();
        options.put("max_file_size", 10485760);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
        String imageUrl = uploadResult.get("url").toString();

        artPiece.setImageUrl(imageUrl);
        artPieceRepository.save(artPiece);

        return imageUrl;
    }

    public ArtPieceResponse updateArtPiece(ArtPieceRequest request, String traceId){
        log.info("updateArtPiece started, traceId: {}", traceId);
        ArtPieceResponse response;
        ValidationResponse validationResponse = artPieceFieldValidationService.updateArtPieceFieldValidation(request, traceId);

        if(!anyValidationErrorExists(validationResponse)) {

            ArtPiece artPiece = artPieceRepository.findByArtId(Integer.valueOf(request.getArtId()));

            if(artPiece != null) {
                if(request.getTitle() != null) artPiece.setTitle(request.getTitle());
                if(request.getDescription() != null) artPiece.setDescription(request.getDescription());
                if(request.getPrice() != null) artPiece.setPrice(request.getPrice().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                if(request.getArtist() != null) artPiece.setArtist(request.getArtist());
                if(request.getYear() != null) artPiece.setYear(Integer.valueOf(request.getYear()));
                if(request.getDimensions() != null) artPiece.setDimensions(request.getDimensions());
                if(request.getMedium() != null) artPiece.setMedium(request.getMedium());
                artPiece.setModifiedAt(LocalDateTime.now());

                if(request.getCategoryId() != null) {
                    Categories category = artCategoryRepository.findByCategoryId(Integer.valueOf(request.getCategoryId()));
                    if (category != null) {
                        artPiece.setCategory(category);
                    } else {
                        log.info("updateArtPiece ended, traceId: {}", traceId);
                        return mapper.mapNoCategoryFoundResponse(traceId);
                    }
                }
                artPieceRepository.save(artPiece);
                response = mapper.mapUpdateSuccessResponse(artPiece,traceId);
            } else {
                response = mapper.mapNoArtPieceFoundResponse(traceId);
            }
        } else {
            response = mapper.mapValidationErrorResponse(validationResponse,traceId);
        }
        log.info("updateArtPiece ended, traceId: {}", traceId);
        return response;
    }

    public List<ArtPieceResponse> getArtPieces(String categoryId, String traceId){
        log.info("getArtPieces started, traceId: {}", traceId);
        List<ArtPieceResponse> responses = new ArrayList<>();
        ArtPieceResponse response = new ArtPieceResponse();

        List<ArtPiece> artPieces = artPieceRepository.findArtPiecesByCategoryId(Integer.valueOf(categoryId));

        if(artPieces != null && !artPieces.isEmpty()) {
            log.info("getArtPieces ended, traceId: {}", traceId);
            return artPieces.stream().map(this::convertToDTO).collect(Collectors.toList());

        }
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No Art Pieces found for this Categories");
        responses.add(response);
        log.info("getArtPieces ended, traceId: {}", traceId);
        return responses;
    }

    public ArtPieceResponse getArtPiece(String artId, String traceId){
        log.info("getArtPiece started, traceId: {}", traceId);
        ArtPieceResponse artPieceResponse;
        ArtPiece artPiece = artPieceRepository.findByArtId(Integer.valueOf(artId));

        if(artPiece != null) {
            log.info("getArtPiece ended, traceId: {}", traceId);
            return convertToDTO(artPiece);
        }
        artPieceResponse = mapper.mapNoArtPieceFoundResponse(traceId);
        log.info("getArtPiece ended, traceId: {}", traceId);
        return artPieceResponse;
    }

    @Transactional
    public ArtPieceResponse deleteArtPiece(String artPieceId, String traceId) {
        log.info("deleteArtPiece started, traceId: {}", traceId);
        ArtPieceResponse artPieceResponse;

        ArtPiece artPiece = artPieceRepository.findByArtId(Integer.valueOf(artPieceId));

        if(artPiece != null){
            if (orderItemsRepository.existsByArtId(Integer.valueOf(artPieceId))) {
                artPiece.setActive(false);
                artPieceRepository.save(artPiece);
                artPieceResponse = mapper.mapDeleteFailedResponse(traceId);
            } else {
                artPieceRepository.deleteById(Integer.valueOf(artPieceId));
                artPieceResponse = mapper.mapDeleteSuccessResponse(traceId);
            }
        } else {
            artPieceResponse = mapper.mapNoArtPieceFoundResponse(traceId);
        }
        log.info("deleteArtPiece ended, traceId: {}", traceId);
        return artPieceResponse;
    }

    private ArtPieceResponse convertToDTO(ArtPiece artPiece){
        ArtPieceResponse response =  new ArtPieceResponse();
        response.setArtId(artPiece.getArtId());
        response.setTitle(artPiece.getTitle());
        response.setDescription(artPiece.getDescription());
        response.setPrice(artPiece.getPrice());
        response.setImageUrl(artPiece.getImageUrl());
        response.setArtist(artPiece.getArtist());
        response.setYear(artPiece.getYear().toString());
        response.setDimensions(artPiece.getDimensions());
        response.setMedium(artPiece.getMedium());
        response.setCategory(artPiece.getCategory().getName());
        return response;
    }
}
