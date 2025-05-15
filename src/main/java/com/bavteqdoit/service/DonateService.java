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
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DonateService {
    private final CurrencyService currencyService;
    private final BalanceService balanceService;
    private final BoxService boxService;

    public void donate(Long id, String acronym, BigDecimal amount) {
        Objects.requireNonNull(id);
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }
        if (acronym == null || acronym.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acronym cannot be empty");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than 0");
        }
        Currency currency = currencyService.getCurrencyByAcronym(acronym);

        if (currency == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found for acronym: " + acronym);
        }


        List<Balance> balances = balanceService.getBalancesByBoxId(id);
        Box box = boxService.getBoxById(id);
        if (balances.isEmpty()) {
            Balance balance = balanceService.addBalance(box, currency, amount);
            boxService.addToBox(id, convertDonationAmountToBoxCurrency(id, balance, amount));
        } else {
            boolean found = false;
            for (Balance balance : balances) {
                if (balance.getCurrency().equals((currency))) {
                    balance.setAmount(balance.getAmount().add(amount));
                    balanceService.updateBalance(balance.getId(), balance);
                    boxService.addToBox(id, convertDonationAmountToBoxCurrency(id, balance, amount));
                    found = true;
                    break;
                }
            }
            if (!found) {
                Balance balance = balanceService.addBalance(box, currency, amount);
                boxService.addToBox(id, convertDonationAmountToBoxCurrency(id, balance, amount));
            }
        }
    }

    public BigDecimal convertDonationAmountToBoxCurrency(Long boxId, Balance balance, BigDecimal amount) {
        Objects.requireNonNull(balance);
        Objects.requireNonNull(boxId);
        if (boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }

        Currency boxCurrency = boxService.getBoxById(boxId).getFundraisingEvent().getAccount().getChosenCurrency();
        Currency balanceCurrency = balance.getCurrency();
        if (balanceCurrency.equals(boxCurrency)) {
            return amount;
        } else if (boxCurrency.getAcronym().equals("USD")) {
            return convertToDollar(amount, balanceCurrency.getRateToDollar());
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
