package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Balance getBalanceById(Long id) {
        return balanceRepository.findById(id).orElse(null);
    }

    public List<Balance> getAllBalances() {
        return balanceRepository.findAll();
    }

    public Balance addBalance(Balance balance) {
        return balanceRepository.save(balance);
    }

    public Balance addBalance(Box box, Currency currency, BigDecimal amount) {
        Balance balance = new Balance();
        balance.setBox(box);
        balance.setCurrency(currency);
        balance.setAmount(amount);
        return balanceRepository.save(balance);
    }

    public List getBalanceByBoxId(long boxId) {
        return balanceRepository.findByBoxId(boxId);
    }

    public Balance updateBalance(long id, Balance updatedBalance) {
        Balance existingBalance = getBalanceById(id);

        existingBalance.setAmount(updatedBalance.getAmount());
        existingBalance.setBox(updatedBalance.getBox());
        existingBalance.setCurrency(updatedBalance.getCurrency());

        return balanceRepository.save(existingBalance);
    }

    public void deleteBalance(long id) {
        balanceRepository.deleteById(id);
    }

    public List<Balance> getBalanceByBox(long boxId) {
        List<Balance> balances = balanceRepository.findByBoxId(boxId);
        List<Balance> newBalances = new ArrayList<>();
        for (Balance balance : balances) {
            if (balance.getBox().getId() == boxId) {
                newBalances.add(balance);
            }
        }
        return newBalances;
    }

}
