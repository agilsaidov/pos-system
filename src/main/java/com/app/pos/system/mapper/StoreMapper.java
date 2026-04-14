package com.app.pos.system.mapper;

import com.app.pos.system.dto.response.StoreDetailResponse;
import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.model.Store;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreResponse toStoreResponse(Store store);

    StoreDetailResponse toStoreDetailResponse(Store store);
}
