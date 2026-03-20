package com.app.pos.system.controller;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/pos/sales")
@RequiredArgsConstructor
public class PosSaleController {

    private final SaleService saleService;

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getSales(@RequestParam(required = false) Long saleId,
                                                       @RequestParam(required = true) Long storeId,
                                                       @RequestParam(required = true) Long cashierId,
                                                       @RequestParam(required = false) OffsetDateTime from,
                                                       @RequestParam(required = false) OffsetDateTime to,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(saleService.getSalesForCashier(saleId, cashierId,storeId, from, to, page, size));

    }
}
