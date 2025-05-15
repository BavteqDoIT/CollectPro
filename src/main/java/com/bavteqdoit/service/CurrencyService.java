package com.bavteqdoit.service;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Currency getCurrencyById(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency id is invalid: " + id);
        }
        return currencyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found"));
    }

    public Currency getCurrencyByAcronym(String acronym) {
        if (acronym.isBlank()) {
            throw new IllegalArgumentException("Acronym cannot be blank");
        }
        return currencyRepository.findByAcronym(acronym).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found"));
    }

    public Currency addCurrency(Currency currency) {
        Objects.requireNonNull(currency, "Currency cannot be null");
        return currencyRepository.save(currency);
    }

    public Currency updateCurrency(Currency currency) {
        Objects.requireNonNull(currency, "Currency cannot be null");
        if (currency.getId() == null || currency.getId() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid currency id");
        }
        return currencyRepository.save(currency);
    }

    public void deleteCurrency(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency id is invalid: " + id);
        }
        getCurrencyById(id);
        currencyRepository.deleteById(id);
    }
}
