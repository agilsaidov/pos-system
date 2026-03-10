package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.DetailedCashierReportResponse;
import com.app.pos.system.projection.DetailedCashierReportProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailedReportMapper {
    DetailedCashierReportResponse toResponse(DetailedCashierReportProjection detailedCashierReportProjection);
}
