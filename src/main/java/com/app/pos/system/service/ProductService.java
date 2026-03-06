package com.app.pos.system.service;

import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.model.Product;
import com.app.pos.system.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;

    public Product getByBarcode(String barcode){
        return productRepo.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "Product not found: " + barcode)
                );
    }
}
