package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.request.CreatePromotionRequest;
import com.app.pos.system.dto.response.PromotionResponse;
import com.app.pos.system.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping
    public ResponseEntity<Page<PromotionResponse>> getPromotions(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) OffsetDateTime startsAt,
            @RequestParam(required = false) OffsetDateTime endsAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(promotionService.getPromotions(name, active, startsAt, endsAt, page, size));
    }

    @PostMapping
    public ResponseEntity<PromotionResponse> createPromotion(@RequestBody CreatePromotionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(promotionService.createPromotion(request));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> togglePromotionActive(@RequestParam Boolean active,
                                                      @PathVariable(name = "id") Long promotionId){
        promotionService.togglePromotionActive(promotionId, active);
        return ResponseEntity.noContent().build();
    }
}
