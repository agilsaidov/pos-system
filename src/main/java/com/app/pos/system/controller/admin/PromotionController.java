package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.request.CreatePromotionRequest;
import com.app.pos.system.dto.response.PromotionResponse;
import com.app.pos.system.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

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
