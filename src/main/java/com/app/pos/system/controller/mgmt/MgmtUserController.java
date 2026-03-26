package com.app.pos.system.controller.mgmt;

import com.app.pos.system.dto.response.CashierDetailsResponse;
import com.app.pos.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mgmt/users")
@RequiredArgsConstructor
public class MgmtUserController {

    private final UserService userService;

    @GetMapping("/cashiers")
    public ResponseEntity<List<CashierDetailsResponse>> getCashiers(@RequestParam Long storeId){
        return ResponseEntity.ok().body(userService.getCashiers(storeId));
    }

    @GetMapping("/cashiers/{cashierId}")
    public ResponseEntity<CashierDetailsResponse> getCashier(
            @PathVariable Long cashierId,
            @RequestParam Long storeId){
        return ResponseEntity.ok().body(userService.getCashier(cashierId, storeId));
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
