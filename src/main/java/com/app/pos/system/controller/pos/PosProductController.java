package com.app.pos.system.controller.pos;

import com.app.pos.system.dto.response.ProductLookupResponse;
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
public class PosProductController {

    private final ProductService productService;

    @GetMapping("/lookup")
    public ResponseEntity<ProductLookupResponse> lookup(@RequestParam String barcode){
        return ResponseEntity.ok().body(productService.lookup(barcode));
    }
}
