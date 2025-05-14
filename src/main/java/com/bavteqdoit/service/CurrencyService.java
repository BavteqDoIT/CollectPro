package com.bavteqdoit.service;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency getCurrencyByAcronym(String acronym) {
        return currencyRepository.findByAcronym(acronym).orElse(null);
    }

    public List<Currency> getAllCurrencies(){
        return currencyRepository.findAll();
    }

    public Currency getCurrencyById(long id) {
        return currencyRepository.findById(id).orElse(null);
    }

    public Currency updateCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }
}
