package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public List<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/{id}")
    public Currency getCurrencyById(@PathVariable Long id) {
        return currencyService.getCurrencyById(id);
    }

    @PostMapping
    public Currency addCurrency(@RequestBody Currency currency) {
        return currencyService.addCurrency(currency);
    }

    @PutMapping("/{id}")
    public Currency updateCurrency(@PathVariable Long id,
                                   @Valid @RequestBody Currency updatedCurrency) {
        Currency existingCurrency = getCurrencyById(id);

        existingCurrency.setAcronym(updatedCurrency.getAcronym());
        existingCurrency.setName(updatedCurrency.getName());
        existingCurrency.setRateToDollar(updatedCurrency.getRateToDollar());

        return currencyService.updateCurrency(existingCurrency);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
