package com.app.pos.system.service;

import com.app.pos.system.dto.response.StoreDetailResponse;
import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.StoreMapper;
import com.app.pos.system.model.Store;
import com.app.pos.system.repo.StoreAssignmentRepository;
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
    private final StoreAssignmentRepository storeAssignmentRepository;

    public Page<StoreResponse> getStores(String name, String city, String address, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return storeRepository.findAll(StoreSpec.withFilters(name, city, address), pageable)
                .map(storeMapper::toStoreResponse);
    }


    public StoreResponse getStoreById(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(
                        () -> new NotFoundException(
                                "STORE_NOT_FOUND",
                                "Store with id " + storeId + " not found"));

        return storeMapper.toStoreResponse(store);
    }


    public StoreDetailResponse getStoreDetails(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(
                        () -> new NotFoundException(
                                "STORE_NOT_FOUND",
                                "Store with id " + storeId + " not found"));

        Integer numberOfStaff = storeAssignmentRepository.countStaff(storeId);

        StoreDetailResponse response = storeMapper.toStoreDetailResponse(store);
        response.setTotalStaff(numberOfStaff);

        return response;
    }
}
