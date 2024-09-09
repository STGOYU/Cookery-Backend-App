package com.cookerytech.repository;

import com.cookerytech.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b WHERE b.isActive = true")
    Page<Brand> getActiveBrands(Pageable pageable);

    @Query("select count(b) from  Brand b where b.isActive=true ")
    long numberOfPublishedBrand();


}
