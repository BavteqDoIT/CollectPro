package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public List<Balance> getAllBalances() {
        return balanceService.getAllBalances();
    }

    @GetMapping("/{id}")
    public Balance getBalanceById(@PathVariable long id) {
        return balanceService.getBalanceById(id);
    }

    @PostMapping
    public Balance addBalance(@Valid @RequestBody Balance balance) {
        return balanceService.addBalance(balance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBalance(@PathVariable long id) {
        balanceService.deleteBalance(id);
        return ResponseEntity.noContent().build();
    }
}
