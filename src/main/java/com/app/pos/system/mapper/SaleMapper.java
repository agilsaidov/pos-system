package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.CheckoutResponse;
import com.app.pos.system.dto.response.SaleItemResponse;
import com.app.pos.system.dto.response.SaleResponse;
import com.app.pos.system.model.Sale;
import com.app.pos.system.model.SaleItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {
    @Mapping(source = "store.storeId", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "cashier.userId", target = "cashierId")
    @Mapping(source = "cashier.firstName", target = "firstName")
    @Mapping(source = "cashier.lastName", target = "lastName")
    @Mapping(source = "createdAt", target = "issueDate")
    SaleResponse toResponse(Sale sale);


    CheckoutResponse toCheckoutResponse(Sale sale);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "taxRateSnapshot", target = "taxRate")
    @Mapping(source = "unitPriceSnapshot", target = "unitPrice")
    SaleItemResponse toSaleItemResponse(SaleItem saleItem);
}
