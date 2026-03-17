package com.app.pos.system.service;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.mapper.SaleMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
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

    public Page<SaleResponse> getSales(Long managerId, Long saleId, Long cashierId, Long storeId,
                                       OffsetDateTime from, OffsetDateTime to,
                                       int page, int size){

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(managerId, storeId))){
            throw new AccessDeniedException("Access denied for manager with id " + managerId);
        }

        Pageable pageable = PageRequest.of(page,size);

        return saleRepository.findAll(
                SaleSpec.withFilters(saleId, cashierId, storeId, from, to), pageable)
                .map(saleMapper::toResponse);
    }

}
