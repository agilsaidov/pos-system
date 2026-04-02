package com.app.pos.system.service;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.SaleMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.specification.SaleSpec;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final AuthUtils authUtils;

    public Page<SaleResponse> getSales(Long saleId, Long cashierId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        if(storeId == null && authUtils.isManager()){
            throw new BadRequestException("STORE_ID_REQUIRED", "Managers must specify a storeId");
        }

        if(storeId != null) {
            validateStoreAccess(storeId);
        }

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                SaleSpec.withFilters(saleId, cashierId, storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }



    public Page<SaleResponse> getSalesForCashier(Long saleId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        UUID cashierId = authUtils.getCurrentUserKeycloakId();
        User cashier = userRepository.findByKeycloakId(cashierId)
                .orElseThrow(() -> new NotFoundException("CASHIER_NOT_FOUND", "Cashier not found"));

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                        SaleSpec.withFilters(saleId, cashier.getUserId(), storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }


    private void validateStoreAccess(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(authUtils.isManager()) {
            UUID keycloakId = authUtils.getCurrentUserKeycloakId();
            User manager = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new NotFoundException("MANAGER_NOT_FOUND", "Manager not found"));

            if (!storeAssignmentRepository.existsById(new StoreAssignmentId(manager.getUserId(), storeId))) {
                throw new AccessDeniedException("Manager with id " + manager.getUserId() + " does not have access to store " + storeId);
            }
        }
    }

}
