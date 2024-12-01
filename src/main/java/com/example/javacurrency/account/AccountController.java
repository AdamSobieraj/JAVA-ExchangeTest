package com.example.javacurrency.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequestMapping("/api/v1/accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {

    public static final String ACCOUNT_NOT_FOUND = "Account not found";
    public static final String UPDATE_ACCOUNT_NOT_FOUND = "Update account not found";
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@Valid @RequestBody UserAccount account) {
        try {
            return ResponseEntity.ok(accountService.createAccount(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getaccount/{firstname}/{lastname}")
    public ResponseEntity<?> getAccountDetailsByFirstLAstName(@PathVariable String firstname, @PathVariable String lastname) {
        UserAccount account = accountService.getAccountDetails(firstname, lastname);
        return !ObjectUtils.isEmpty(account) ?
                ResponseEntity.ok(account) : ResponseEntity.badRequest().body(ACCOUNT_NOT_FOUND);
    }

    @GetMapping("/getaccountuuid/{uuid}")
    public ResponseEntity<?> getAccountDetailsByUUID(@PathVariable String uuid) {
        UserAccount account = accountService.getAccountDetailsUUID(uuid);
        return !ObjectUtils.isEmpty(account) ?
                ResponseEntity.ok(account) : ResponseEntity.badRequest().body(ACCOUNT_NOT_FOUND);
    }

    @DeleteMapping("/delete/{firstname}/{lastname}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String firstname, @PathVariable String lastname) {
        accountService.deleteAccount(firstname, lastname);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAccount(@RequestBody UserAccount updatedAccount) {
        UserAccount account = accountService.updateAccount(updatedAccount);
        return !ObjectUtils.isEmpty(account) ?
                ResponseEntity.ok(account) : ResponseEntity.badRequest().body(UPDATE_ACCOUNT_NOT_FOUND);
    }

    @PostMapping("/exchange")
    public ResponseEntity<?> exchangeCurrency(@Valid @RequestBody AccExchangeCurrencyReq accExchangeCurrencyReq) {
        UserAccount account = accountService.exchangeCurrency(accExchangeCurrencyReq);
        return !ObjectUtils.isEmpty(account) ?
                ResponseEntity.ok(account) : ResponseEntity.badRequest().body(ACCOUNT_NOT_FOUND);
    }

}

