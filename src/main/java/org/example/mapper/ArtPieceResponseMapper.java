package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.ArtPiece;
import org.example.json.ArtPieceResponse;
import org.example.json.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArtPieceResponseMapper {

    public ArtPieceResponse mapSuccessResponse(ArtPiece artPiece, String traceId) {
        log.info("map success response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setArtId(artPiece.getArtId().toString());
        response.setTitle(artPiece.getTitle());
        response.setDescription(artPiece.getDescription());
        response.setImageUrl(artPiece.getImageUrl());
        response.setPrice(artPiece.getPrice());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Piece added successfully..!!");
        log.info("map success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapValidationErrorResponse(ValidationResponse validationResponse, String traceId) {
        log.info("map validation error response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setValidationResponse(validationResponse.getValidationErrors().getErrorList().toString());
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setMessage("Field validation failed");
        log.info("map validation error response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapUpdateSuccessResponse(ArtPiece artPiece, String traceId) {
        log.info("map update success response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setArtId(artPiece.getArtId().toString());
        response.setTitle(artPiece.getTitle());
        response.setDescription(artPiece.getDescription());
        response.setImageUrl(artPiece.getImageUrl());
        response.setPrice(artPiece.getPrice());
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Piece updated successfully..!!");
        log.info("map update success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapNoCategoryFoundResponse(String traceId) {
        log.info("map no category found response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No category found");
        log.info("map no category found response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapNoArtPieceFoundResponse(String traceId) {
        log.info("map no art piece found response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setMessage("No art piece found");
        log.info("map no art piece found response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapDeleteSuccessResponse(String traceId) {
        log.info("map delete success response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.OK);
        response.setMessage("Art Piece deleted successfully...!!!");
        log.info("map delete success response ended, traceId: {}", traceId);
        return response;
    }

    public ArtPieceResponse mapDeleteFailedResponse(String traceId) {
        log.info("map delete failed response started, traceId: {}", traceId);
        ArtPieceResponse response = new ArtPieceResponse();
        response.setStatus(HttpStatus.CONFLICT);
        response.setMessage("Cannot delete art piece as it has been ordered, therefore set it as inactive instead");
        log.info("map delete failed response ended, traceId: {}", traceId);
        return response;
    }
}
