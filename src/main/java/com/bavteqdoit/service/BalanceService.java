package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.service.BoxService;
import com.bavteqdoit.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Balance findBalanceById(Long id) {
        return balanceRepository.findById(id).orElse(null);
    }

    public List<Balance> findAllBalances() {
        return balanceRepository.findAll();
    }

    public Balance addBalance(Balance balance) {
        return balanceRepository.save(balance);
    }

    public Balance addBalance(Box box, Currency currency, BigDecimal amount) {
        Balance balance = new Balance();
        balance.setBox(box);
        balance.setCurrencyId(currency);
        balance.setAmount(amount);
        return balanceRepository.save(balance);
    }

    public List findBalanceByBoxId(long boxId) {
        return balanceRepository.findByBoxId(boxId);
    }

    public Balance updateBalance(long id, Balance updatedBalance) {
        Balance existingBalance = balanceRepository.findById(id).orElse(null);

        existingBalance.setAmount(updatedBalance.getAmount());
        existingBalance.setBox(updatedBalance.getBox());
        existingBalance.setCurrencyId(updatedBalance.getCurrencyId());

        return balanceRepository.save(existingBalance);
    }

    public void deleteBalance(long id) {
        balanceRepository.deleteById(id);
    }


}
