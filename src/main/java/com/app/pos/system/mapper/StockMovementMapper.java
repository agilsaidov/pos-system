package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.StockMovementResponse;
import com.app.pos.system.model.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.barcode", target = "productBarcode")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "stockMovementType", target = "type")
    StockMovementResponse toResponse(StockMovement movement);
}
