package com.app.pos.system.service;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.exception.NotFoundException;
import com.app.pos.system.mapper.UserMapper;
import com.app.pos.system.model.StoreAssignment;
import com.app.pos.system.model.User;
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
    private final UserMapper userMapper;

    public List<CashierDetailsResponse> getCashiers(Long storeId){

        if(!storeRepository.existsById(storeId)){
            throw new NotFoundException("STORE_NOT_FOUND", "Store with id " + storeId + " not found");
        }

        return storeAssignmentRepository.findAllByStoreId(storeId)
                .stream().map(userMapper::toResponse)
                .toList();
    }
}
