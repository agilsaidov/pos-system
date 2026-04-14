package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.model.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toResponse(Store store);
}
