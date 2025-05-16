package com.bavteqdoit.service;

import com.bavteqdoit.entity.Balance;
import com.bavteqdoit.entity.Box;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.repository.BalanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceServiceTest {

    private BalanceRepository balanceRepository;
    private BalanceService balanceService;

    @BeforeEach
    void setUp() {
        balanceRepository = mock(BalanceRepository.class);
        balanceService = new BalanceService(balanceRepository);
    }

    @Test
    void getAllBalances_returnsAll() {
        List<Balance> balances = List.of(new Balance(), new Balance());
        when(balanceRepository.findAll()).thenReturn(balances);

        List<Balance> result = balanceService.getAllBalances();

        assertEquals(2, result.size());
        verify(balanceRepository).findAll();
    }

    @Test
    void getBalanceById_validId_returnsBalance() {
        Balance balance = new Balance();
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        Balance result = balanceService.getBalanceById(1L);

        assertNotNull(result);
        verify(balanceRepository).findById(1L);
    }

    @Test
    void getBalanceById_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> balanceService.getBalanceById(0L));
    }

    @Test
    void getBalanceById_notFound_throwsException() {
        when(balanceRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> balanceService.getBalanceById(999L));
    }

    @Test
    void addBalance_withObject_savesBalance() {
        Balance balance = new Balance();
        when(balanceRepository.save(balance)).thenReturn(balance);

        Balance result = balanceService.addBalance(balance);

        assertEquals(balance, result);
        verify(balanceRepository).save(balance);
    }

    @Test
    void addBalance_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> balanceService.addBalance((Balance) null));
    }

    @Test
    void testAddBalance_withDetails_createsAndSavesBalance() {
        Box box = new Box();
        Currency currency = new Currency();
        BigDecimal amount = BigDecimal.TEN;

        Balance saved = new Balance();
        saved.setBox(box);
        saved.setCurrency(currency);
        saved.setAmount(amount);

        when(balanceRepository.save(any(Balance.class))).thenReturn(saved);

        Balance result = balanceService.addBalance(box, currency, amount);

        assertEquals(saved, result);
        assertEquals(box, result.getBox());
        assertEquals(currency, result.getCurrency());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void testAddBalance_withNulls_throwsException() {
        assertThrows(NullPointerException.class, () -> balanceService.addBalance(null, new Currency(), BigDecimal.ONE));
        assertThrows(NullPointerException.class, () -> balanceService.addBalance(new Box(), null, BigDecimal.ONE));
        assertThrows(NullPointerException.class, () -> balanceService.addBalance(new Box(), new Currency(), null));
    }

    @Test
    void getBalancesByBoxId_valid_returnsList() {
        List<Balance> balances = List.of(new Balance(), new Balance());
        when(balanceRepository.findByBoxId(1L)).thenReturn(balances);

        List<Balance> result = balanceService.getBalancesByBoxId(1L);

        assertEquals(2, result.size());
        verify(balanceRepository).findByBoxId(1L);
    }

    @Test
    void getBalancesByBoxId_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> balanceService.getBalancesByBoxId(0));
    }

    @Test
    void updateBalance_valid_updatesEntity() {
        Balance existing = new Balance();
        existing.setAmount(BigDecimal.ONE);
        existing.setBox(new Box());
        existing.setCurrency(new Currency());

        Balance updated = new Balance();
        updated.setAmount(BigDecimal.TEN);
        updated.setBox(new Box());
        updated.setCurrency(new Currency());

        when(balanceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(balanceRepository.save(any(Balance.class))).thenReturn(existing);

        balanceService.updateBalance(1L, updated);

        verify(balanceRepository).save(existing);
        assertEquals(BigDecimal.TEN, existing.getAmount());
    }

    @Test
    void updateBalance_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> balanceService.updateBalance(0, new Balance()));
    }

    @Test
    void updateBalance_nullUpdated_throwsException() {
        assertThrows(NullPointerException.class, () -> balanceService.updateBalance(1, null));
    }

    @Test
    void deleteBalance_zeroBalance_deletes() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.ZERO);
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        balanceService.deleteBalance(1L);

        verify(balanceRepository).deleteById(1L);
    }

    @Test
    void deleteBalance_nonZero_throwsConflict() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.ONE);
        when(balanceRepository.findById(1L)).thenReturn(Optional.of(balance));

        assertThrows(ResponseStatusException.class, () -> balanceService.deleteBalance(1L));
    }

    @Test
    void deleteBalance_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> balanceService.deleteBalance(0L));
    }

    @Test
    void getBalanceByBox_validBox_returnsFilteredList() {
        Box box = new Box();
        box.setId(1L);

        Balance b1 = new Balance();
        b1.setBox(box);

        Balance b2 = new Balance();
        b2.setBox(box);

        when(balanceRepository.findByBoxId(1L)).thenReturn(List.of(b1, b2));

        List<Balance> result = balanceService.getBalanceByBox(1L);

        assertEquals(2, result.size());
        assertTrue(result.contains(b1));
        assertTrue(result.contains(b2));
    }

    @Test
    void getBalanceByBox_emptyList_throwsNotFound() {
        when(balanceRepository.findByBoxId(1L)).thenReturn(new ArrayList<>());

        assertThrows(ResponseStatusException.class, () -> balanceService.getBalanceByBox(1L));
    }

    @Test
    void getBalanceByBox_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> balanceService.getBalanceByBox(0L));
    }
}
