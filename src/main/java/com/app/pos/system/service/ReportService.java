package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.dto.response.DailyReportResponse;
import com.app.pos.system.dto.response.DetailedCashierReportResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.ReportMapper;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.SaleRepository;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final AuthUtils authUtils;

    private final ReportMapper reportMapper;

    @Transactional(readOnly = true)
    public List<CashierReportResponse> getCashiersReports(Long storeId, OffsetDateTime from, OffsetDateTime to){

        if (authUtils.isManager()) validateStoreAccess(storeId);

        OffsetDateTime effectiveFrom = from != null ? from : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime effectiveTo = to != null ? to : OffsetDateTime.now();

        return saleRepository.getReports(storeId, effectiveFrom, effectiveTo)
                .stream().map(reportMapper::toCashierReportResponse).toList();

    }

    @Transactional(readOnly = true)
    public DetailedCashierReportResponse getDetailedCashierReport(Long cashierId, Long storeId, OffsetDateTime from, OffsetDateTime to){
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

        if (authUtils.isManager()) validateStoreAccess(storeId);

        OffsetDateTime effectiveFrom = from != null ? from : OffsetDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime effectiveTo = to != null ? to : OffsetDateTime.now();

        return reportMapper.toDetailedCashierReportResponse(saleRepository.getDetailedReport(cashierId, storeId, effectiveFrom, effectiveTo));

    }


    @Transactional(readOnly = true)
    public DailyReportResponse getDailyReport(Long storeId, LocalDate date){

        if (authUtils.isManager()) validateStoreAccess(storeId);

        return reportMapper.toDailyReportResponse(saleRepository.getDailyReport(storeId, date));
    }


    private void validateStoreAccess(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        UUID keycloakId = authUtils.getCurrentUserKeycloakId();
        User manager = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("MANAGER_NOT_FOUND", "Manager not found"));

        if (!storeAssignmentRepository.existsById(new StoreAssignmentId(manager.getUserId(), storeId))) {
            throw new AccessDeniedException("Manager with id " + manager.getUserId() + " does not have access to store " + storeId);
        }
    }
}
