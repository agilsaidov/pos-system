package com.app.pos.system.controller.pos;

import com.app.pos.system.dto.request.CheckoutRequest;
import com.app.pos.system.dto.response.CheckoutResponse;
import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/pos/sales")
@RequiredArgsConstructor
public class PosSaleController {

    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getSalesForCashier(@RequestParam(required = false) Long saleId,
                                                       @RequestParam(required = false) Long storeId,
                                                       @RequestParam(required = false) OffsetDateTime from,
                                                       @RequestParam(required = false) OffsetDateTime to,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(saleService.getSalesForCashier(saleId, storeId, from, to, page, size));

    }


    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestBody @Valid CheckoutRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.checkout(request));
    }
}
