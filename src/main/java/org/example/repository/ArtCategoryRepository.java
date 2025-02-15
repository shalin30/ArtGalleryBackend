package org.example.repository;

import org.example.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtCategoryRepository extends JpaRepository<Categories, Integer> {
    Categories findByCategoryId(Integer categoryId);

    List<Categories> findAll();
}
