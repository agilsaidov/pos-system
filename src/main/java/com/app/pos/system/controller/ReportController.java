package com.app.pos.system.controller;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.dto.response.DailyReportResponse;
import com.app.pos.system.dto.response.DetailedCashierReportResponse;
import com.app.pos.system.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/mgmt/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/cashiers")
    public ResponseEntity<List<CashierReportResponse>> getReports(
            @RequestParam(required = true) Long storeId,
            @RequestParam(required = false) OffsetDateTime from,
            @RequestParam(required = false) OffsetDateTime to
            ){

        return ResponseEntity.ok().body(reportService.getReports(storeId, from, to));
    }

    @GetMapping("/cashiers/{cashierId}")
    public ResponseEntity<DetailedCashierReportResponse> getDetailedReport(@PathVariable Long cashierId,
                                                                           @RequestParam(required = true) Long storeId,
                                                                           @RequestParam(required = false) OffsetDateTime from,
                                                                           @RequestParam(required = false) OffsetDateTime to){

        return ResponseEntity.ok().body(reportService.getDetailedReport(cashierId, storeId, from, to));
    }

    @GetMapping("/daily")
    public ResponseEntity<DailyReportResponse> getDailyReport(@RequestParam(required = true) Long storeId,
                                                              @RequestParam(required =  true) LocalDate date){

        return ResponseEntity.ok().body(reportService.getDailyReport(storeId, date));
    }
}
