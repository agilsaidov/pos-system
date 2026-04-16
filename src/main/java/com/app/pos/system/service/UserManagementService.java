package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.exception.AccessDeniedException;
import com.app.pos.system.exception.BadRequestException;
import com.app.pos.system.exception.DuplicateException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.UserMapper;
import com.app.pos.system.model.Store;
import com.app.pos.system.model.StoreAssignment;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.model.User;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import com.app.pos.system.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final StoreAssignmentRepository storeAssignmentRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final AuthUtils authUtils;

    public List<CashierDetailsResponse> getCashiers(Long storeId){

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(
                        "STORE_NOT_FOUND", "Store with id " + storeId + " not found"));

        validateStoreAccess(store.getStoreId());

        return storeAssignmentRepository.findAllByStoreId(storeId)
                .stream().map(userMapper::toCashierDetailsResponse)
                .toList();
    }


    public CashierDetailsResponse getCashier(Long cashierId, Long storeId){

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(
                        "STORE_NOT_FOUND", "Store with id " + storeId + " not found"));

        validateStoreAccess(store.getStoreId());

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE", "Cashier not found in the specified store");
        }

        return userMapper.toCashierDetailsResponse(storeAssignmentRepository.getByUserIdAndStoreId(cashierId, storeId));

    }


    @Transactional
    public void assignCashierToStore(Long cashierId, Long storeId){

        validateStoreActivity(storeId);
        validateStoreAccess(storeId);

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if (storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))) {
            throw new DuplicateException("ALREADY_ASSIGNED", "Cashier already assigned to this store");
        }

        StoreAssignment assignment = new StoreAssignment();
        assignment.setStoreAssignmentId(new StoreAssignmentId(cashierId, storeId));
        assignment.setUser(userRepository.getReferenceById(cashierId));
        assignment.setStore(storeRepository.getReferenceById(storeId));
        storeAssignmentRepository.save(assignment);
    }


    @Transactional
    public void unAssignCashierFromStore(Long cashierId, Long storeId){

        validateStoreActivity(storeId);
        validateStoreAccess(storeId);

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE", "Cashier not found in the specified store");
        }

        storeAssignmentRepository.deleteById(new StoreAssignmentId(cashierId, storeId));
    }


    private void validateStoreAccess(Long storeId) {
        if(authUtils.isManager()) {
            UUID keycloakId = authUtils.getCurrentUserKeycloakId();
            User manager = userRepository.findByKeycloakId(keycloakId)
                    .orElseThrow(() -> new NotFoundException("MANAGER_NOT_FOUND", "Manager not found"));


            if (!storeAssignmentRepository.existsById(new StoreAssignmentId(manager.getUserId(), storeId))) {
                throw new AccessDeniedException("Manager with id " + manager.getUserId() + " does not have access to store " + storeId);
            }
        }
    }


    private Store validateStoreActivity(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(
                        "STORE_NOT_FOUND", "Store with id " + storeId + " not found"));

        if(!store.getActive()){
            throw new BadRequestException("STORE_INACTIVE", "Store is not active");
        }

        return store;
    }
}
