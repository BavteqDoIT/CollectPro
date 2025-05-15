package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping("/organization/{organizationId}/currency/{acronym}")
    public Account createAccount(@PathVariable Long organizationId,
                                 @PathVariable String acronym,
                                 @Valid @RequestBody Account account) {
        return accountService.createAccount(acronym, organizationId, account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccountById(id);
        return ResponseEntity.noContent().build();
    }
}
