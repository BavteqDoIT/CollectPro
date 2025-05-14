package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.service.BalanceService;
import lombok.RequiredArgsConstructor;
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
    public Balance addBalance(@RequestBody Balance balance) {
        return balanceService.addBalance(balance);
    }

    @DeleteMapping("/{id}")
    public void deleteBalance(@PathVariable long id) {
        balanceService.deleteBalance(id);
    }
}
