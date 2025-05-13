package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public List<Currency> getAllCurrencies(){
        return currencyService.findAllCurrencies();
    }

    @PostMapping
    public Currency addCurrency(@RequestBody Currency currency){
        return currencyService.addCurrency(currency);
    }
}
