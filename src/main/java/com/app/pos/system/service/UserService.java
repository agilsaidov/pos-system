package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.exception.DuplicateException;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.UserMapper;
import com.app.pos.system.model.StoreAssignment;
import com.app.pos.system.model.StoreAssignmentId;
import com.app.pos.system.repo.StoreAssignmentRepository;
import com.app.pos.system.repo.StoreRepository;
import com.app.pos.system.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StoreAssignmentRepository storeAssignmentRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<CashierDetailsResponse> getCashiers(Long storeId){

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        return storeAssignmentRepository.findAllByStoreId(storeId)
                .stream().map(userMapper::toResponse)
                .toList();
    }


    public CashierDetailsResponse getCashier(Long cashierId, Long storeId){
        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE", "Cashier not found in the specified store");
        }

        return userMapper.toResponse(storeAssignmentRepository.getByUserIdAndStoreId(cashierId, storeId));

    }


    public void assignCashierToStore(Long cashierId, Long storeId){
        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

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


    public void unAssignCashierFromStore(Long cashierId, Long storeId){
        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        if(!userRepository.existsById(cashierId)){
            throw new NotFoundException("CASHIER_NOT_FOUND", "Cashier with id " + cashierId + " not found");
        }

        if(!storeAssignmentRepository.existsById(new StoreAssignmentId(cashierId, storeId))){
            throw new NotFoundException("CASHIER_NOT_IN_STORE", "Cashier not found in the specified store");
        }

        storeAssignmentRepository.deleteById(new StoreAssignmentId(cashierId, storeId));
    }
}
