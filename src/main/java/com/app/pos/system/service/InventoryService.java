package com.app.pos.system.service;

import com.app.pos.system.dto.response.InventoryResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.InventoryMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.repo.InventoryRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
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
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final StoreRepository storeRepository;

    public Page<InventoryResponse> getInventory(Long managerId, Long storeId, int page, int size){

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(managerId, storeId))){
            throw new AccessDeniedException("Access denied for manager with id " + managerId);
        }

        Pageable pageable = PageRequest.of(page,size);

        return inventoryRepo.getInventoryByStoreStoreId(storeId, pageable)
                .map(inventoryMapper::toResponse);
    }
}
