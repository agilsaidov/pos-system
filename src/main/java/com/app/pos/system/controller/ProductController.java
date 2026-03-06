package com.app.pos.system.controller;

import com.app.pos.system.model.Product;
import com.app.pos.system.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pos/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/lookup")
    public ResponseEntity<Product> lookup(@RequestParam String barcode){
        return ResponseEntity.ok().body(productService.getByBarcode(barcode));
    }
}
