package com.app.pos.system.service;

import com.app.pos.system.dto.request.StoreRequest;
import com.app.pos.system.dto.response.StoreDetailResponse;
import com.app.pos.system.dto.response.StoreResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.StoreMapper;
import com.app.pos.system.model.Store;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.specification.StoreSpec;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final StoreAssignmentRepository storeAssignmentRepository;
    private final AuthUtils authUtils;
    private final UserRepository userRepository;

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

        validateStoreAccess(store);

        return storeMapper.toStoreResponse(store);
    }


    public StoreDetailResponse getStoreDetails(Long storeId){
        Store store = storeRepository.findById(storeId).orElseThrow(
                        () -> new NotFoundException(
                                "STORE_NOT_FOUND",
                                "Store with id " + storeId + " not found"));

        validateStoreAccess(store);

        Integer numberOfStaff = storeAssignmentRepository.countStaff(storeId);

        StoreDetailResponse response = storeMapper.toStoreDetailResponse(store);
        response.setTotalStaff(numberOfStaff);

        return response;
    }


    public StoreDetailResponse createStore(StoreRequest request){
        Store store = Store.builder()
                        .name(request.getName())
                        .city(request.getCity())
                        .address(request.getAddress())
                        .phone(request.getPhone())
                        .openedAt(request.getOpenedAt())
                        .build();

        Store savedStore = storeRepository.save(store);

        StoreDetailResponse response = storeMapper.toStoreDetailResponse(savedStore);
        response.setTotalStaff(storeAssignmentRepository.countStaff(savedStore.getStoreId()));

        return response;
    }


    public void toggleStoreActive(Long storeId, Boolean active){
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new NotFoundException(
                        "STORE_NOT_FOUND",
                        "Store with id " + storeId + " not found"));

        store.setActive(active);
        storeRepository.save(store);
    }


    public StoreDetailResponse updateStore(Long storeId, StoreRequest request) {
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new NotFoundException(
                        "STORE_NOT_FOUND",
                        "Store with id " + storeId + " not found"));

        if (Boolean.TRUE.equals(store.getActive())) {
            throw new BadRequestException("CANNOT_UPDATE_ACTIVE_STORE",
                    "Store is currently active. Deactivate the store before changing core details." +
                    " Note: Don't update store in work hours");
        }

        store.setName(request.getName());
        store.setCity(request.getCity());
        store.setAddress(request.getAddress());
        store.setPhone(request.getPhone());
        store.setOpenedAt(request.getOpenedAt());

        return storeMapper.toStoreDetailResponse(storeRepository.save(store));
    }


    private void validateStoreAccess(Store store) {

        UUID keycloakId = authUtils.getCurrentUserKeycloakId();
        User currentUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found"));

        if (!authUtils.isAdmin()) {
            boolean isAssigned = storeAssignmentRepository
                    .existsById(new StoreAssignmentId(currentUser.getUserId(), store.getStoreId()));

            if (!isAssigned) {
                throw new AccessDeniedException("You do not have permission to access this store.");
            }
        }
    }
}
