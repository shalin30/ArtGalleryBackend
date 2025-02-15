package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.json.*;
import org.example.json.Response;
import org.example.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ArtController {

    @Autowired
    ArtCategoryOrchestratorService artCategoryOrchestratorService;

    @Autowired
    ArtPieceOrchestratorService artPieceOrchestratorService;

    public ArtController(ArtCategoryOrchestratorService artCategoryOrchestratorService, ArtPieceOrchestratorService artPieceOrchestratorService) {
        this.artCategoryOrchestratorService = artCategoryOrchestratorService;
        this.artPieceOrchestratorService = artPieceOrchestratorService;
    }

    @PostMapping("/admin/insert-art-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtCategoryResponse> insertArtCategory(@RequestBody ArtCategoryRequest artCategoryRequest,
                                                      @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("InsertArtCategory Controller started, traceId : {}", traceId);
        ArtCategoryResponse response = artCategoryOrchestratorService.insertArtCategory(artCategoryRequest, traceId);
        log.info("InsertArtCategory Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/admin/update-art-category")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtCategoryResponse> updateArtCategory(@RequestBody ArtCategoryRequest artCategoryRequest,
                                                      @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("updateArtCategory Controller started, traceId : {}", traceId);
        ArtCategoryResponse response = artCategoryOrchestratorService.updateArtCategory(artCategoryRequest, traceId);
        log.info("updateArtCategory Controller ended, traceId : {}", traceId);
            return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/admin/delete-art-category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtCategoryResponse> deleteArtCategory(@PathVariable String categoryId,
                                                           @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("deleteArtCategory Controller started, traceId : {}", traceId);
        ArtCategoryResponse response = artCategoryOrchestratorService.deleteArtCategory(categoryId, traceId);
        log.info("deleteArtCategory Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<ArtCategoryResponse>> getArtCategories(@RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getArtCategories Controller started, traceId : {}", traceId);
        List<ArtCategoryResponse> response = artCategoryOrchestratorService.getArtCategories(traceId);
        log.info("getArtCategories Controller ended, traceId : {}", traceId);
        if(response == null || response.isEmpty() || response.get(0).getStatus().equals(HttpStatus.NOT_FOUND)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/admin/insert-art-piece")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtPieceResponse> insertArtPiece(@RequestBody ArtPieceRequest artPieceRequest,
                                                      @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("insertArtPiece Controller started, traceId : {}", traceId);
        ArtPieceResponse response = artPieceOrchestratorService.insertArtPiece(artPieceRequest, traceId);
        log.info("insertArtPiece Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/admin/update-art-piece")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtPieceResponse> updateArtPiece(@RequestBody ArtPieceRequest artPieceRequest,
                                                      @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("updateArtPiece Controller started, traceId : {}", traceId);
        ArtPieceResponse response = artPieceOrchestratorService.updateArtPiece(artPieceRequest, traceId);
        log.info("updateArtPiece Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/admin/delete-art-piece/{artId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ArtPieceResponse> deleteArtPiece(@PathVariable String artId,
                                                           @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("deleteArtPiece Controller started, traceId : {}", traceId);
        ArtPieceResponse response = artPieceOrchestratorService.deleteArtPiece(artId, traceId);
        log.info("deleteArtPiece Controller ended, traceId : {}", traceId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ArtPieceResponse>> getArtPieces(@PathVariable String categoryId,
                                                               @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getArtPieces Controller started, traceId : {}", traceId);
        List<ArtPieceResponse> response = artPieceOrchestratorService.getArtPieces(categoryId, traceId);
        log.info("getArtPieces Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/pieces/{artId}")
    public ResponseEntity<ArtPieceResponse> getArtPiece(@PathVariable String artId,
                                                               @RequestHeader(required = false, name = "TraceId") String traceId){
        log.info("getArtPiece Controller started, traceId : {}", traceId);
        ArtPieceResponse response = artPieceOrchestratorService.getArtPiece(artId, traceId);
        log.info("getArtPiece Controller ended, traceId : {}", traceId);
        if(response != null){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
