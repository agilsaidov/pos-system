package com.app.pos.system.service;

import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.mapper.StoreMapper;
import com.app.pos.system.model.Store;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.specification.StoreSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;

    public Page<StoreResponse> getStores(String name, String city, String address, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return storeRepository.findAll(StoreSpec.withFilters(name, city, address), pageable)
                .map(storeMapper::toResponse);
    }
}
