package com.app.pos.system.service;

import com.app.pos.system.dto.request.ProductRequest;
import com.app.pos.system.dto.response.ProductResponse;
import com.app.pos.system.exception.DuplicateProductException;
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
                        "No product found with barcode: " + barcode)
                );
    }

    public ProductResponse createProduct(ProductRequest request){

        if(productRepo.existsByBarcode(request.getBarcode())){
            throw new DuplicateProductException(
                    "Product with barcode " + request.getBarcode() + " already exists"
            );
        }

        Product saved = productRepo.save(mapToEntity(request));
        return mapToResponse(saved);
    }



    private Product mapToEntity(ProductRequest request) {
        Product product = new Product();
        product.setBarcode(request.getBarcode());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setCost(request.getCost());
        product.setTaxRate(request.getTaxRate());
        product.setActive(request.getActive() != null ? request.getActive() : true);
        return product;
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setProductId(product.getProductId());
        response.setBarcode(product.getBarcode());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setCost(product.getCost());
        response.setTaxRate(product.getTaxRate());
        response.setActive(product.getActive());
        return response;
    }
}
