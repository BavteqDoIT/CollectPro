package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public List<Balance> getAllBalances() {
        return balanceRepository.findAll();
    }

    public Balance getBalanceById(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        return balanceRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Balance not found"));
    }

    public Balance addBalance(Balance balance) {
        Objects.requireNonNull(balance);
        return balanceRepository.save(balance);
    }

    public Balance addBalance(Box box, Currency currency, BigDecimal amount) {
        Objects.requireNonNull(currency, "Currency is required");
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(box, "Box is required");

        Balance balance = new Balance();
        balance.setBox(box);
        balance.setCurrency(currency);
        balance.setAmount(amount);

        return balanceRepository.save(balance);
    }

    public List<Balance> getBalancesByBoxId(long boxId) {
        if (boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid box ID");
        }
        return balanceRepository.findByBoxId(boxId);
    }

    public void updateBalance(long id, Balance updatedBalance) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Objects.requireNonNull(updatedBalance, "Balance cannot be null");
        Balance existingBalance = getBalanceById(id);

        existingBalance.setAmount(updatedBalance.getAmount());
        existingBalance.setBox(updatedBalance.getBox());
        existingBalance.setCurrency(updatedBalance.getCurrency());

        balanceRepository.save(existingBalance);
    }

    public void deleteBalance(long id) {
        if(id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        if (getBalanceById(id).getAmount().compareTo(BigDecimal.ZERO) == 0) {
            balanceRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete non-zero balance");
        }
    }

    public List<Balance> getBalanceByBox(long boxId) {
        if (boxId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid box ID");
        }
        List<Balance> balances = balanceRepository.findByBoxId(boxId);
        List<Balance> newBalances = new ArrayList<>();
        if(balances.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Box balances not found");
        } else {
            for (Balance balance : balances) {
                if (balance.getBox().getId() == boxId) {
                    newBalances.add(balance);
                }
            }
            return newBalances;
        }
    }
}
