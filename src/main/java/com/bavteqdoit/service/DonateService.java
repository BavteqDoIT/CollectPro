package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonateService {
    private final CurrencyService currencyService;
    private final BalanceService balanceService;
    private final BoxService boxService;

    public void donate(long id, String acronym, BigDecimal amount) {
        Currency currency = currencyService.findCurrencyByAcronym(acronym);

        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Currency not found");
        }

        List<Balance> balances = balanceService.getBalanceByBoxId(id);
        for (Balance balance : balances) {
            if (balance.getCurrencyId().equals((currency))) {
                balance.setAmount(balance.getAmount().add(amount));
                balanceService.updateBalance(balance.getId(), balance);
                break;
            }
        }
        Box box = boxService.getBoxById(id);
        balanceService.addBalance(box, currency, amount);
    }
}
