package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.CashierReportMapper;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final CashierReportMapper cashierReportMapper;

    public List<CashierReportResponse> getReports(Long storeId, OffsetDateTime from, OffsetDateTime to){
        if(!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        OffsetDateTime effectiveFrom = from != null ? from : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime effectiveTo = to != null ? to : OffsetDateTime.now();

        return saleRepository.getReports(storeId, effectiveFrom, effectiveTo)
                .stream().map(cashierReportMapper::toResponse).toList();

    }
}
