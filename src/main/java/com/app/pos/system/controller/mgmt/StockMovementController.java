package com.app.pos.system.controller.mgmt;

import com.app.pos.system.dto.request.StockMovementRequest;
import com.app.pos.system.dto.response.StockMovementResponse;
import com.app.pos.system.service.StockMovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/mgmt/stock-movement")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> getStockMovements(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) OffsetDateTime from,
            @RequestParam(required = false) OffsetDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(stockMovementService.getStockMovement(storeId, productId, from, to, page, size));

    }

    @PostMapping
    public ResponseEntity<StockMovementResponse> createStockMovement(
            @RequestBody @Valid StockMovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockMovementService.createStockMovement(request));
    }
}
