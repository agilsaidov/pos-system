package com.app.pos.system.service;

import com.app.pos.system.dto.request.ProductRequest;
import com.app.pos.system.dto.response.ProductLookupResponse;
import com.app.pos.system.dto.response.ProductResponse;
import com.app.pos.system.exception.DuplicateProductException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.ProductMapper;
import com.app.pos.system.model.Product;
import com.app.pos.system.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;

    public ProductLookupResponse lookup(String barcode){
        Product product = productRepo.findByBarcode(barcode)
                .orElseThrow(() -> new NotFoundException(
                        "PRODUCT_NOT_FOUND",
                        "No product found with barcode: " + barcode)
                );

        return productMapper.toLookupResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request){

        if(productRepo.existsByBarcode(request.getBarcode())){
            throw new DuplicateProductException(
                    "Product with barcode " + request.getBarcode() + " already exists"
            );
        }

        Product saved = productRepo.save(productMapper.toEntity(request));
        return productMapper.toResponse(saved);
    }


    public ProductResponse updateProduct(Long id, ProductRequest request){
        Product product = productRepo.getProductByProductId(id)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product with id " + id + " not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setTaxRate(request.getTaxRate());
        product.setActive(request.getActive());

        return productMapper.toResponse(product);
    }
}
