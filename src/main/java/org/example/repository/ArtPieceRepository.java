package org.example.repository;

import org.example.entity.ArtPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtPieceRepository extends JpaRepository<ArtPiece, Integer> {
    ArtPiece findByArtId(Integer artId);

    @Query("SELECT a FROM ArtPiece a WHERE a.category.categoryId = :categoryId")
    List<ArtPiece> findArtPiecesByCategoryId(@Param("categoryId") Integer categoryId);
}
