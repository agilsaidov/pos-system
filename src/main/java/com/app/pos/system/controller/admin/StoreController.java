package com.app.pos.system.controller.admin;

import com.app.pos.system.dto.response.StoreDetailResponse;
import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<Page<StoreResponse>> getStores(@RequestParam(required = false) String name,
                                                         @RequestParam(required = false) String city,
                                                         @RequestParam(required = false) String address,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size){

        return ResponseEntity.ok().body(storeService.getStores(name, city, address, page, size));
    }


    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable("id") Long storeId){
        return ResponseEntity.ok().body(storeService.getStoreById(storeId));
    }


    @GetMapping("/{id}/details")
    public ResponseEntity<StoreDetailResponse> getStoreDetails(@PathVariable("id") Long storeId){
        return ResponseEntity.ok().body(storeService.getStoreDetails(storeId));
    }
}
