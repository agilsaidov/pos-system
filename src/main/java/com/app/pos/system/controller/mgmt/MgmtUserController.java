package com.app.pos.system.controller.mgmt;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mgmt/users")
@RequiredArgsConstructor
public class MgmtUserController {

    private final UserManagementService userService;

    @GetMapping("/cashiers/stores/{storeId}")
    public ResponseEntity<List<CashierDetailsResponse>> getCashiers(@PathVariable Long storeId){
        return ResponseEntity.ok(userService.getCashiers(storeId));
    }

    @GetMapping("/cashiers/{cashierId}/stores/{storeId}")
    public ResponseEntity<CashierDetailsResponse> getCashier(
            @PathVariable Long cashierId,
            @PathVariable Long storeId){
        return ResponseEntity.ok(userService.getCashier(cashierId, storeId));
    }

    @PostMapping("/cashiers/{cashierId}/stores/{storeId}")
    public ResponseEntity<Void> assignToStore(@PathVariable Long cashierId,
                                              @PathVariable Long storeId){

        userService.assignCashierToStore(cashierId, storeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/cashiers/{cashierId}/stores/{storeId}")
    public ResponseEntity<Void> unAssignFromStore(@PathVariable Long cashierId,
                                                  @PathVariable Long storeId){

        userService.unAssignCashierFromStore(cashierId, storeId);
        return ResponseEntity.noContent().build();
    }

}
