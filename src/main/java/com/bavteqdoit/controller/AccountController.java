package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.service.AccountService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{organizationId}/{acronym}")
    public Account createAccount(@PathVariable Long organizationId, @PathVariable String acronym, @RequestBody Account account) {
        return accountService.createAccount(acronym, organizationId, account);
    }

    @DeleteMapping("/{id}")
    public List<Account> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccountById(id);
    }
}
