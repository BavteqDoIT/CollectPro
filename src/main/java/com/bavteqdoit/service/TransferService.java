package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final BalanceService balanceService;
    private final AccountService accountService;
    private final BoxService boxService;
    private final DonateService donateService;

    public Box transferMoneyToAccount(Long boxId) {
        if (boxId == null || boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Box id must be greater than 0");
        }
        Box existingBox = boxService.getBoxById(boxId);
        Account account = existingBox.getFundraisingEvent().getAccount();
        account.setBalance(existingBox.getSum().subtract(charge(boxId)).add(account.getBalance()));
        clearBox(existingBox);
        accountService.persistAccount(account);
        return existingBox;
    }

    public void clearBox(Box box) {
        Objects.requireNonNull(box, "Box cannot be null");
        box.setSum(BigDecimal.ZERO);
        clearAllBalances(box.getId());
    }

    public void clearAllBalances(Long boxId) {
        if(boxId == null || boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Box id must be greater than 0");
        }
        List<Balance> balances = balanceService.getBalanceByBox(boxId);
        for (Balance balance : balances) {
            balance.setAmount(BigDecimal.ZERO);
        }
    }

    public void deleteBalances(Long boxId) {
        if (boxId == null || boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Box id must be greater than 0");
        }
        List<Balance> balances= balanceService.getBalanceByBox(boxId);
        for (Balance balance : balances) {
            balanceService.deleteBalance(balance.getId());
        }
    }

    public Box endRental(Long boxId) {
        if (boxId == null || boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Box id must be greater than 0");
        }
        Box existingBox = boxService.getBoxById(boxId);
        transferMoneyToAccount(boxId);
        deleteBalances(boxId);
        existingBox.setRented(false);
        existingBox.setStartDate(null);
        existingBox.setEndDate(null);
        existingBox.setFundraisingEvent(null);
        return boxService.updateBox(existingBox.getId(),existingBox);
    }

    public BigDecimal charge(Long boxId) {
        if(boxId == null || boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Box id must be greater than 0");
        }
        Box existingBox = boxService.getBoxById(boxId);

        long days = ChronoUnit.DAYS.between(existingBox.getStartDate(), existingBox.getEndDate());
        if(days <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Days must be greater than 0");
        }

        BigDecimal dollarRateOfChosenCurrency = existingBox.getFundraisingEvent().getAccount().getChosenCurrency().getRateToDollar();

        return BigDecimal.valueOf(days).multiply(donateService.convertFromDollarToCurrency(existingBox.getPrice(), dollarRateOfChosenCurrency));
    }
}
