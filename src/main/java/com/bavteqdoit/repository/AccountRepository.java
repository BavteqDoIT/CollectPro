package com.bavteqdoit.repository;

import com.bavteqdoit.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
