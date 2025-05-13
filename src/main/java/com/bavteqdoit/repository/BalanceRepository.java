package com.bavteqdoit.repository;

import com.bavteqdoit.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    List<Balance> findByBoxId(long boxId);
}
