package com.bavteqdoit.repository;

import com.bavteqdoit.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository <Currency, Long>{
}
