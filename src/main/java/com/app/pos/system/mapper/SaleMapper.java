package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.model.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(source = "store.storeId", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "cashier.userId", target = "cashierId")
    @Mapping(source = "cashier.fullName", target = "cashierName")
    @Mapping(source = "createdAt", target = "issueDate")
    SaleResponse toResponse(Sale sale);
}
