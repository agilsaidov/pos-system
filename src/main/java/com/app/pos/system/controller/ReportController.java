package com.app.pos.system.controller;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
