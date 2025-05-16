package com.bavteqdoit.service;

import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyServiceTest {

    private CurrencyRepository currencyRepository;
    private CurrencyService currencyService;

    @BeforeEach
    void setUp() {
        currencyRepository = mock(CurrencyRepository.class);
        currencyService = new CurrencyService(currencyRepository);
    }

    @Test
    void getAllCurrencies_returnsAll() {
        List<Currency> currencies = List.of(new Currency(), new Currency());
        when(currencyRepository.findAll()).thenReturn(currencies);

        List<Currency> result = currencyService.getAllCurrencies();

        assertEquals(2, result.size());
        verify(currencyRepository).findAll();
    }

    @Test
    void getCurrencyById_validId_returnsCurrency() {
        Currency currency = new Currency();
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        Currency result = currencyService.getCurrencyById(1L);

        assertEquals(currency, result);
    }

    @Test
    void getCurrencyById_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> currencyService.getCurrencyById(0L));
    }

    @Test
    void getCurrencyById_notFound_throwsException() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> currencyService.getCurrencyById(1L));
    }

    @Test
    void getCurrencyByAcronym_validAcronym_returnsCurrency() {
        Currency currency = new Currency();
        when(currencyRepository.findByAcronym("USD")).thenReturn(Optional.of(currency));

        Currency result = currencyService.getCurrencyByAcronym("USD");

        assertEquals(currency, result);
    }

    @Test
    void getCurrencyByAcronym_blank_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> currencyService.getCurrencyByAcronym("  "));
    }

    @Test
    void getCurrencyByAcronym_notFound_throwsException() {
        when(currencyRepository.findByAcronym("EUR")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> currencyService.getCurrencyByAcronym("EUR"));
    }

    @Test
    void addCurrency_validCurrency_returnsSaved() {
        Currency currency = new Currency();
        when(currencyRepository.save(currency)).thenReturn(currency);

        Currency result = currencyService.addCurrency(currency);

        assertEquals(currency, result);
    }

    @Test
    void addCurrency_null_throwsException() {
        assertThrows(NullPointerException.class, () -> currencyService.addCurrency(null));
    }

    @Test
    void updateCurrency_validCurrency_returnsUpdated() {
        Currency currency = new Currency();
        currency.setId(1L);
        when(currencyRepository.save(currency)).thenReturn(currency);

        Currency result = currencyService.updateCurrency(currency);

        assertEquals(currency, result);
    }

    @Test
    void updateCurrency_null_throwsException() {
        assertThrows(NullPointerException.class, () -> currencyService.updateCurrency(null));
    }

    @Test
    void updateCurrency_invalidId_throwsException() {
        Currency currency = new Currency();
        currency.setId(0L);

        assertThrows(ResponseStatusException.class, () -> currencyService.updateCurrency(currency));
    }

    @Test
    void updateCurrency_nullId_throwsException() {
        Currency currency = new Currency();
        currency.setId(null);

        assertThrows(ResponseStatusException.class, () -> currencyService.updateCurrency(currency));
    }

    @Test
    void deleteCurrency_validId_deletes() {
        Currency currency = new Currency();
        when(currencyRepository.findById(1L)).thenReturn(Optional.of(currency));

        currencyService.deleteCurrency(1L);

        verify(currencyRepository).deleteById(1L);
    }

    @Test
    void deleteCurrency_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> currencyService.deleteCurrency(0L));
    }

    @Test
    void deleteCurrency_notFound_throwsException() {
        when(currencyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> currencyService.deleteCurrency(1L));
    }
}
