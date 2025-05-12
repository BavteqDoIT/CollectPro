package com.bavteqdoit.service;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public Currency addCurrency(Currency currency) {
        return currencyRepository.save(currency);
    }

    public Currency findCurrencyByAcronym(String acronym) {
        return currencyRepository.findByAcronym(acronym).orElse(null);
    }
}
