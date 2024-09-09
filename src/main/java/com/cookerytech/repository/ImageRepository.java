package com.cookerytech.repository;

import com.cookerytech.domain.Image;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image,String> {

    @EntityGraph(attributePaths = "id")
    Optional<Image> findImageById(String imageId);

}
