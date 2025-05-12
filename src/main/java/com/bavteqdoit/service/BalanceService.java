package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Balance findById(Long id) {
        return balanceRepository.findById(id).orElse(null);
    }
}
