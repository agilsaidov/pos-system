package com.app.pos.system.service;

import com.app.pos.system.dto.response.InventoryResponse;
import com.app.pos.system.mapper.InventoryMapper;
import com.app.pos.system.repo.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper inventoryMapper;

    public Page<InventoryResponse> getInventory(Long storeId, int page, int size){

        Pageable pageable = PageRequest.of(page,size);

        return inventoryRepo.getInventoryByStoreStoreId(storeId, pageable)
                .map(inventory -> inventoryMapper.toResponse(inventory));
    }
}
