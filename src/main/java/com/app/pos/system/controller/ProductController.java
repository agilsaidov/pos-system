package com.app.pos.system.controller;

import com.app.pos.system.model.Product;
import com.app.pos.system.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api/pos/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping("/lookup")
    public ResponseEntity<Optional<Product>> getProduct(@RequestParam String barcode){
        Optional<Product> prod = productRepository.findByBarcode(barcode);
        if(prod.isPresent()) {
            return ResponseEntity.ok().body(prod);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
}
