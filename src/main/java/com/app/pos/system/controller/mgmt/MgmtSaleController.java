package com.app.pos.system.controller.mgmt;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/mgmt/sales")
@RequiredArgsConstructor
public class MgmtSaleController {

    private final SaleService salesService;

    @GetMapping
    public ResponseEntity<Page<SaleResponse>> getSales(
            @RequestParam Long managerId,
            @RequestParam(required = false) Long saleId,
            @RequestParam(required = false) Long cashierId,
            @RequestParam Long storeId,
            @RequestParam(required = false) OffsetDateTime from,
            @RequestParam(required = false) OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(salesService.getSalesForManager(managerId, saleId, cashierId, storeId, from, to, page, size));

    }
}
