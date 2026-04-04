package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.request.ProductRequest;
import com.app.pos.system.dto.response.ProductResponse;
import com.app.pos.system.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @RequestBody ProductRequest request){

        return ResponseEntity.ok().body(productService.updateProduct(id, request));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(
                 @RequestParam(required = false) String search,
                 @RequestParam(required = false) String barcode,
                 @RequestParam(defaultValue = "0") int page,
                 @RequestParam(defaultValue = "10") int size
    ){

        return ResponseEntity.ok().body(productService.getProducts(search, barcode, page, size));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> toggleProductActive(@PathVariable Long id,
                                                    @RequestParam("active") Boolean active){

        productService.toggleProductActive(id, active);
        return ResponseEntity.noContent().build();
    }
}
