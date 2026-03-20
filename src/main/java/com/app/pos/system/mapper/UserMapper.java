package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.dto.response.UserResponse;
import com.app.pos.system.model.User;
import com.app.pos.system.projection.CashierDetailsProjection;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CashierDetailsResponse toCashierDetailsResponse(CashierDetailsProjection cashierDetailsProjection);
    UserResponse toUserResponse(User user);
}
