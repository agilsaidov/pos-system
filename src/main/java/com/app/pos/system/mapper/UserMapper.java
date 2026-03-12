package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.projection.CashierDetailsProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CashierDetailsResponse toResponse(CashierDetailsProjection cashierDetailsProjection);
}
