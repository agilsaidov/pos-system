package com.app.pos.system.service;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.SaleMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.specification.SaleSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public Page<SaleResponse> getSalesForManager(Long managerId, Long saleId, Long cashierId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){


        if(!userRepository.existsById(managerId)){
            throw new NotFoundException("MANAGER_NOT_FOUND", "Manager with id " + managerId + " not found");
        }

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(managerId, storeId))){
            throw new AccessDeniedException("Access denied for manager with id " + managerId);
        }

        if(saleId != null){
            if(!saleRepository.existsById(saleId)){
                throw new NotFoundException("SALE_NOT_FOUND", "Sale with id " + saleId + " not found");
            }
        }

        if(cashierId != null){

            if(!userRepository.existsById(cashierId)){
                throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
            }

            if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
                throw new NotFoundException(
                        "CASHIER_NOT_IN_STORE",
                        "Cashier with id " + cashierId + " is not assigned to store " + storeId
                );
            }
        }

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                SaleSpec.withFilters(saleId, cashierId, storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }





    public Page<SaleResponse> getSalesForCashier(Long saleId, Long cashierId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE", "Cashier not found in specified store");
        }

        if(saleId != null){
            if(!saleRepository.existsById(saleId)){
                throw new NotFoundException("SALE_NOT_FOUND", "Sale with id " + saleId + " not found");
            }
        }


        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                        SaleSpec.withFilters(saleId, cashierId, storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }

}
