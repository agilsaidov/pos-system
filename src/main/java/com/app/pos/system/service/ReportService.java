package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.dto.response.DetailedCashierReportResponse;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.CashierReportMapper;
import com.app.pos.system.mapper.DetailedReportMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final CashierReportMapper cashierReportMapper;

    private final DetailedReportMapper detailedReportMapper;

    @Transactional(readOnly = true)
    public List<CashierReportResponse> getReports(Long storeId, OffsetDateTime from, OffsetDateTime to){
        if(!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        OffsetDateTime effectiveFrom = from != null ? from : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime effectiveTo = to != null ? to : OffsetDateTime.now();

        return saleRepository.getReports(storeId, effectiveFrom, effectiveTo)
                .stream().map(cashierReportMapper::toResponse).toList();

    }

    @Transactional(readOnly = true)
    public DetailedCashierReportResponse getDetailedReport(Long cashierId, Long storeId, OffsetDateTime from, OffsetDateTime to){
        if(!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE",
                    "Cashier not found in the specified store");
        }

        OffsetDateTime effectiveFrom = from != null ? from : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime effectiveTo = to != null ? to : OffsetDateTime.now();

        return  detailedReportMapper.toResponse(saleRepository.getDetailedReport(cashierId, storeId, effectiveFrom, effectiveTo));

    }
}
