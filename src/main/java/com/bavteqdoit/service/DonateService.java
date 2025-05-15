package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonateService {
    private final CurrencyService currencyService;
    private final BalanceService balanceService;
    private final BoxService boxService;

    public void donate(long id, String acronym, BigDecimal amount) {
        Currency currency = currencyService.getCurrencyByAcronym(acronym);

        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        }


        List<Balance> balances = balanceService.getBalanceByBoxId(id);
        if (balances.isEmpty()) {
            Box box = boxService.getBoxById(id);
            Balance balance = balanceService.addBalance(box, currency, amount);
            boxService.addToBox(id, recalculateDonate(id, balance, amount));
        } else {
            boolean found = false;
            for (Balance balance : balances) {
                if (balance.getCurrency().equals((currency))) {
                    BigDecimal before = balance.getAmount();
                    balance.setAmount(balance.getAmount().add(amount));
                    balanceService.updateBalance(balance.getId(), balance);
                    boxService.addToBox(id, recalculateDonate(id, balance, amount));
                    found = true;
                    break;
                }
            }
            if(!found){
                Box box = boxService.getBoxById(id);
                Balance balance = balanceService.addBalance(box, currency, amount);
                boxService.addToBox(id, recalculateDonate(id, balance, amount));
            }
        }
    }

    public BigDecimal recalculateDonate(Long boxId, Balance balance, BigDecimal amount) {
        Currency boxCurrency = boxService.getBoxById(boxId).getFundraisingEvent().getAccount().getChosenCurrency();
        Currency balanceCurrency = balance.getCurrency();
        if (balanceCurrency.equals(boxCurrency)) {
            return amount;
        } else if (boxCurrency.getAcronym().equals("USD")) {
            return convertToDollar(amount , balanceCurrency.getRateToDollar());
        } else {
            return convertFromDollarToCurrency(convertToDollar(amount, balanceCurrency.getRateToDollar()), boxCurrency.getRateToDollar());
        }
    }

    public BigDecimal convertToDollar(BigDecimal amount, BigDecimal rateToDollar) {
        return amount.multiply(rateToDollar);
    }

    public BigDecimal convertFromDollarToCurrency(BigDecimal amount, BigDecimal rateToDollar) {
        return amount.divide(rateToDollar, 2, RoundingMode.HALF_UP);
    }
}
