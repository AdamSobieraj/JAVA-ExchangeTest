package com.example.javacurrency.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Validated
@RequestMapping("/api/v1/accounts")
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<UserAccount> createAccount(@Valid @RequestBody UserAccount account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping("/getaccount/{firstname}/{lastname}")
    public ResponseEntity<?> getAccountDetailsByFirstLAstName(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            return ResponseEntity.ok(accountService.getAccountDetails(firstname, lastname));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getaccountuuid/{uuid}")
    public ResponseEntity<?> getAccountDetailsByUUID(@PathVariable String uuid) {
        try {
            return ResponseEntity.ok(accountService.getAccountDetailsUUID(uuid));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{firstname}/{lastname}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String firstname, @PathVariable String lastname) {
        accountService.deleteAccount(firstname, lastname);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<UserAccount> updateAccount(@RequestBody UserAccount updatedAccount) {
        return ResponseEntity.ok(accountService.updateAccount(updatedAccount));
    }

    @GetMapping("/exchange/{uuid}")
    public ResponseEntity<?> exchangeCurrency(@PathVariable String uuid) {
        UserAccount result;
        try {
            result = accountService.exchangeCurrency(uuid);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

}

