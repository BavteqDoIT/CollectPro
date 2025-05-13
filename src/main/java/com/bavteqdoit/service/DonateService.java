package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonateService {
    private final CurrencyService currencyService;
    private final BalanceService balanceService;

    public void donate(long id, String acronym, BigDecimal amount) {
        Currency currency = currencyService.findCurrencyByAcronym(acronym);

        if (currency == null) {
            throw new RuntimeException("Currency not found");
        }

        List<Balance> balances = balanceService.findBalanceByBoxId(id);
        for (Balance balance : balances) {
            System.out.println(currency);
            System.out.println(balance.getCurrencyId());
            if (balance.getCurrencyId().equals((currency))) {
                System.out.println("Before donation: " + balance.getAmount()); // Log before donation
                balance.setAmount(balance.getAmount().add(amount));
                System.out.println("After donation: " + balance.getAmount()); // Log after donation
                balanceService.updateBalance(balance.getId(), balance);
                break;
            }
        }


    }
}
