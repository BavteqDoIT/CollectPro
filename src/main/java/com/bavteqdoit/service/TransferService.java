package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final BalanceService balanceService;
    private final AccountService accountService;
    private final BoxService boxService;

    public Box transferMoneyToAccount(long boxId) {
        Box existingBox = boxService.getBoxById(boxId);
        Account account = existingBox.getFundraisingEvent().getAccount();
        account.setBalance(existingBox.getSum().subtract(charge(boxId)).add(account.getBalance()));
        clearBox(existingBox);
        accountService.updateAccountById(account.getId());
        return boxService.getBoxById(boxId);
    }

    public void clearBox(Box box) {
        box.setSum(BigDecimal.ZERO);
        clearAllBalances(box.getId());
    }

    public void clearAllBalances(long boxId) {
        List<Balance> balances = balanceService.getBalanceByBox(boxId);
        for (Balance balance : balances) {
            balance.setAmount(BigDecimal.ZERO);
        }
    }

    public void deleteBalances(long boxId) {
        List<Balance> balances= balanceService.getBalanceByBox(boxId);
        for (Balance balance : balances) {
            balanceService.deleteBalance(balance.getId());
        }
    }

    public Box endRental(long boxId) {
        Box existingBox = boxService.getBoxById(boxId);
        transferMoneyToAccount(boxId);
        deleteBalances(boxId);
        existingBox.setRented(false);
        existingBox.setStartDate(null);
        existingBox.setEndDate(null);
        existingBox.setFundraisingEvent(null);
        return boxService.updateBox(existingBox.getId(),existingBox);
    }

    public BigDecimal charge(long boxId) {
        Box existingBox = boxService.getBoxById(boxId);
        long days = ChronoUnit.DAYS.between(existingBox.getStartDate(), existingBox.getEndDate());
        return BigDecimal.valueOf(days).multiply(existingBox.getPrice());
    }
}
