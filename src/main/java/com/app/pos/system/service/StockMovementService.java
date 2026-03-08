package com.app.pos.system.service;

import com.app.pos.system.dto.response.StockMovementResponse;
import com.app.pos.system.mapper.StockMovementMapper;
import com.app.pos.system.repo.StockMovementRepository;
import com.app.pos.system.specification.StockMovementSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository repository;
    private final StockMovementMapper stockMovementMapper;

    public Page<StockMovementResponse> getStockMovement(Long storeId,
                                                        Long productId,
                                                        OffsetDateTime from,
                                                        OffsetDateTime to,
                                                        int page, int size){

        Pageable pageable = PageRequest.of(page,size);

        return repository.findAll(
                StockMovementSpec.withFilters(storeId, productId, from, to),pageable)
                .map(movement -> stockMovementMapper.toResponse(movement));

    }

}
