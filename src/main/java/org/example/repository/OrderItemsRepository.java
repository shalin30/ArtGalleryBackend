package org.example.repository;

import org.example.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem, Integer> {

    @Query("SELECT o FROM OrderItem o WHERE o.artPiece.artId = :artId")
    Boolean existsByArtId(@Param("artId") Integer artId);
}
