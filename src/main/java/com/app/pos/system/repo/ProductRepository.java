package com.app.pos.system.repo;

import com.app.pos.system.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByBarcode(String barcode);

    Boolean existsByBarcode(String barcode);

    Optional<Product> getProductByProductId(Long id);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
