package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.CashierReportResponse;
import com.app.pos.system.projection.CashierReportProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CashierReportMapper {
    CashierReportResponse toResponse(CashierReportProjection projection);
}
