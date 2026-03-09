package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.InventoryResponse;
import com.app.pos.system.model.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(source = "store.storeId", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.barcode", target = "productBarcode")
    InventoryResponse toResponse(Inventory inventory);
}
