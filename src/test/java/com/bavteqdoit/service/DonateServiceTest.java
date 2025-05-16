package com.bavteqdoit.service;

import com.bavteqdoit.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DonateServiceTest {

    private CurrencyService currencyService;
    private BalanceService balanceService;
    private BoxService boxService;
    private DonateService donateService;

    @BeforeEach
    void setUp() {
        currencyService = mock(CurrencyService.class);
        balanceService = mock(BalanceService.class);
        boxService = mock(BoxService.class);
        donateService = new DonateService(currencyService, balanceService, boxService);
    }

    @Test
    void convertToDollar_shouldMultiplyCorrectly() {
        BigDecimal amount = new BigDecimal("10");
        BigDecimal rate = new BigDecimal("1.5");

        BigDecimal result = donateService.convertToDollar(amount, rate);
        assertEquals(new BigDecimal("15.0"), result);
    }

    @Test
    void convertFromDollarToCurrency_shouldDivideAndRound() {
        BigDecimal amount = new BigDecimal("10");
        BigDecimal rate = new BigDecimal("2");

        BigDecimal result = donateService.convertFromDollarToCurrency(amount, rate);
        assertEquals(new BigDecimal("5.00"), result);
    }

    @Test
    void convertDonationAmountToBoxCurrency_sameCurrency_shouldReturnAmount() {
        Currency currency = new Currency(1L, "PLN","Zloty", BigDecimal.ONE);
        Account account = new Account();
        account.setChosenCurrency(currency);
        FundraisingEvent event = new FundraisingEvent();
        event.setAccount(account);
        Box box = new Box();
        box.setFundraisingEvent(event);

        when(boxService.getBoxById(1L)).thenReturn(box);

        Balance balance = new Balance();
        balance.setCurrency(currency);
        BigDecimal amount = new BigDecimal("100");

        BigDecimal result = donateService.convertDonationAmountToBoxCurrency(1L, balance, amount);

        assertEquals(amount, result);
    }

    @Test
    void convertDonationAmountToBoxCurrency_differentCurrencyToUSD_shouldConvertProperly() {
        Currency fromCurrency = new Currency(1L, "EUR","Euro", new BigDecimal("1.2"));
        Currency toCurrency = new Currency(2L, "USD","US Dollar", new BigDecimal("1.0"));
        Account account = new Account();
        account.setChosenCurrency(toCurrency);
        FundraisingEvent event = new FundraisingEvent();
        event.setAccount(account);
        Box box = new Box();
        box.setFundraisingEvent(event);

        when(boxService.getBoxById(1L)).thenReturn(box);

        Balance balance = new Balance();
        balance.setCurrency(fromCurrency);

        BigDecimal result = donateService.convertDonationAmountToBoxCurrency(1L, balance, new BigDecimal("10"));

        assertEquals(new BigDecimal("12.0"), result); // 10 * 1.2
    }

    @Test
    void convertDonationAmountToBoxCurrency_differentCurrencyNonUSD_shouldConvertTwice() {
        Currency fromCurrency = new Currency(1L, "EUR", "Euro", new BigDecimal("1.2"));
        Currency toCurrency = new Currency(2L, "PLN", "Zloty", new BigDecimal("3.78")); // 1 USD = 3.78 PLN
        Account account = new Account();
        account.setChosenCurrency(toCurrency);
        FundraisingEvent event = new FundraisingEvent();
        event.setAccount(account);
        Box box = new Box();
        box.setFundraisingEvent(event);

        when(boxService.getBoxById(1L)).thenReturn(box);

        Balance balance = new Balance();
        balance.setCurrency(fromCurrency);

        BigDecimal result = donateService.convertDonationAmountToBoxCurrency(1L, balance, new BigDecimal("10"));
        assertEquals(new BigDecimal("3.17"), result); // 10 * 1.2 = 12, 12 / 3.78 = 3.174603174603175
    }

    @Test
    void donate_newBalance_shouldCreateAndAddToBox() {
        Currency currency = new Currency(1L, "USD", "US Dollar", BigDecimal.ONE);

        Account account = Account.builder()
                .chosenCurrency(currency)
                .build();

        FundraisingEvent fundraisingEvent = new FundraisingEvent();
        fundraisingEvent.setAccount(account);

        Box box = new Box();
        box.setFundraisingEvent(fundraisingEvent);

        when(currencyService.getCurrencyByAcronym("USD")).thenReturn(currency);
        when(balanceService.getBalancesByBoxId(1L)).thenReturn(Collections.emptyList());
        when(boxService.getBoxById(1L)).thenReturn(box);

        Balance newBalance = new Balance();
        newBalance.setCurrency(currency);

        when(balanceService.addBalance(any(), any(), any())).thenReturn(newBalance);

        donateService.donate(1L, "USD", new BigDecimal("50"));

        verify(balanceService).addBalance(eq(box), eq(currency), eq(new BigDecimal("50")));
        verify(boxService).addToBox(eq(1L), eq(new BigDecimal("50")));
    }

    @Test
    void donate_existingBalance_shouldUpdateAndAddToBox() {
        Currency currency = new Currency(1L, "USD", "US Dollar", BigDecimal.ONE);
        Account account = Account.builder()
                .id(1L)
                .chosenCurrency(currency)
                .build();

        FundraisingEvent fundraisingEvent = new FundraisingEvent();
        fundraisingEvent.setAccount(account);

        Box box = new Box();
        box.setFundraisingEvent(fundraisingEvent);

        Balance existingBalance = new Balance();
        existingBalance.setId(1L);
        existingBalance.setAmount(new BigDecimal("10"));
        existingBalance.setCurrency(currency);

        when(currencyService.getCurrencyByAcronym("USD")).thenReturn(currency);
        when(balanceService.getBalancesByBoxId(1L)).thenReturn(List.of(existingBalance));
        when(boxService.getBoxById(1L)).thenReturn(box);

        donateService.donate(1L, "USD", new BigDecimal("40"));

        assertEquals(new BigDecimal("50"), existingBalance.getAmount());
        verify(balanceService).updateBalance(eq(1L), eq(existingBalance));
        verify(boxService).addToBox(eq(1L), eq(new BigDecimal("40")));
    }

    @Test
    void donate_invalidId_shouldThrow() {
        assertThrows(ResponseStatusException.class, () -> donateService.donate(0L, "USD", new BigDecimal("10")));
    }

    @Test
    void donate_invalidAmount_shouldThrow() {
        assertThrows(ResponseStatusException.class, () -> donateService.donate(1L, "USD", new BigDecimal("-1")));
    }

    @Test
    void donate_emptyAcronym_shouldThrow() {
        assertThrows(ResponseStatusException.class, () -> donateService.donate(1L, "", new BigDecimal("10")));
    }

    @Test
    void donate_currencyNotFound_shouldThrow() {
        when(currencyService.getCurrencyByAcronym("USD")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> donateService.donate(1L, "USD", new BigDecimal("10")));
    }
}
