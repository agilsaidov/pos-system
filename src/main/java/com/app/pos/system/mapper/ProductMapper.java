package com.app.pos.system.mapper;

import com.app.pos.system.dto.request.ProductRequest;
import com.app.pos.system.dto.response.ProductLookupResponse;
import com.app.pos.system.dto.response.ProductResponse;
import com.app.pos.system.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toResponse(Product product);
    ProductLookupResponse toLookupResponse(Product product);
    Product toEntity(ProductRequest request);
}
