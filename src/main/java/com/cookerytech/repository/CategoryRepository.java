package com.cookerytech.repository;

import com.cookerytech.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByTitle(String title);

    @Query("select count(b) from  Product b where b.isActive=true ")
    long numberOfPublishedCategory();
}
