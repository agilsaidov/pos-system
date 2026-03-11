package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.dto.response.DailyReportResponse;
import com.app.pos.system.dto.response.DetailedCashierReportResponse;
import com.app.pos.system.projection.CashierReportProjection;
import com.app.pos.system.projection.DailyReportProjection;
import com.app.pos.system.projection.DetailedCashierReportProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    DetailedCashierReportResponse toDetailedCashierReportResponse(DetailedCashierReportProjection detailedCashierReportProjection);

    CashierReportResponse toCashierReportResponse(CashierReportProjection projection);

    DailyReportResponse toDailyReportResponse(DailyReportProjection projection);
}
