package com.app.pos.system.service;

import com.app.pos.system.dto.response.InventoryResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.InventoryMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.InventoryRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.security.JwtUtils;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;
    private final InventoryMapper inventoryMapper;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public Page<InventoryResponse> getInventory(Long storeId, int page, int size){

        UUID keycloakId = authUtils.getCurrentUserKeycloakId();

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(authUtils.isManager()){

            User manager = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new NotFoundException("MANAGER_NOT_FOUND", "Manager not found"));

            if(!storeAssignmentRepository.existsById(new StoreAssignmentId(manager.getUserId(), storeId))){
                throw new AccessDeniedException("Access denied for manager with id " + manager.getUserId());
            }
        }

        Pageable pageable = PageRequest.of(page,size);

        return inventoryRepo.getInventoryByStoreStoreId(storeId, pageable)
                .map(inventoryMapper::toResponse);
    }
}
