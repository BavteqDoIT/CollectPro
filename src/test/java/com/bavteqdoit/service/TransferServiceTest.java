package com.bavteqdoit.service;

import com.bavteqdoit.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    private BalanceService balanceService;
    private AccountService accountService;
    private BoxService boxService;
    private DonateService donateService;
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        balanceService = mock(BalanceService.class);
        accountService = mock(AccountService.class);
        boxService = mock(BoxService.class);
        donateService = mock(DonateService.class);

        transferService = new TransferService(balanceService, accountService, boxService, donateService);
    }

    @Test
    void endRental_successfulEndRental_resetsBoxAndDeletesBalances() {
        Long boxId = 1L;

        Box box = new Box();
        box.setId(boxId);
        box.setPrice(BigDecimal.ONE);
        box.setSum(BigDecimal.valueOf(100));
        box.setStartDate(LocalDate.of(2024, 1, 1));
        box.setEndDate(LocalDate.of(2024, 1, 31));
        box.setRented(true);

        Currency currency = new Currency();
        currency.setRateToDollar(BigDecimal.ONE);

        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        account.setId(1L);
        account.setChosenCurrency(currency);

        FundraisingEvent fe = new FundraisingEvent();
        fe.setAccount(account);
        box.setFundraisingEvent(fe);

        Balance balance1 = new Balance();
        balance1.setAmount(BigDecimal.valueOf(30));
        balance1.setCurrency(currency);

        when(boxService.getBoxById(boxId)).thenReturn(box);
        when(balanceService.getBalanceByBox(boxId)).thenReturn(List.of(balance1));
        when(donateService.convertFromDollarToCurrency(any(), any())).thenReturn(BigDecimal.ONE);
        when(boxService.updateBox(eq(boxId), any())).thenAnswer(invocation -> invocation.getArgument(1));

        TransferService spyTransferService = spy(transferService);

        doCallRealMethod().when(spyTransferService).transferMoneyToAccount(boxId);
        doNothing().when(spyTransferService).deleteBalances(boxId);

        Box updatedBox = spyTransferService.endRental(boxId);

        verify(spyTransferService, times(1)).transferMoneyToAccount(boxId);
        verify(spyTransferService, times(1)).deleteBalances(boxId);
        verify(boxService, times(1)).updateBox(eq(boxId), any());

        assertFalse(updatedBox.isRented());
        assertNull(updatedBox.getStartDate());
        assertNull(updatedBox.getEndDate());
        assertNull(updatedBox.getFundraisingEvent());
    }

    @Test
    void clearBox_setsSumToZero() {
        Box box = new Box();
        box.setId(1L);
        box.setSum(BigDecimal.valueOf(100));

        transferService.clearBox(box);

        assertEquals(BigDecimal.ZERO, box.getSum());
    }

    @Test
    void clearAllBalances_zerosAmountsInBalances() {
        Long boxId = 1L;

        Balance b1 = new Balance();
        b1.setId(10L);
        b1.setAmount(BigDecimal.valueOf(50));

        Balance b2 = new Balance();
        b2.setId(20L);
        b2.setAmount(BigDecimal.valueOf(30));

        when(balanceService.getBalanceByBox(boxId)).thenReturn(List.of(b1, b2));

        transferService.clearAllBalances(boxId);

        assertEquals(BigDecimal.ZERO, b1.getAmount());
        assertEquals(BigDecimal.ZERO, b2.getAmount());

        verify(balanceService).getBalanceByBox(boxId);

        verify(balanceService, never()).deleteBalance(anyLong());
    }


    @Test
    void deleteBalances_deletesBalancesByBoxId() {
        Long boxId = 1L;

        Balance b1 = new Balance();
        b1.setId(100L);
        Balance b2 = new Balance();
        b2.setId(200L);

        when(balanceService.getBalanceByBox(boxId)).thenReturn(List.of(b1, b2));

        transferService.deleteBalances(boxId);

        verify(balanceService, times(1)).deleteBalance(100L);
        verify(balanceService, times(1)).deleteBalance(200L);
    }

    @Test
    void endRental_updatesBoxAndReturnsUpdatedBox() {
        Long boxId = 1L;

        Box box = new Box();
        box.setId(boxId);
        box.setStartDate(LocalDate.of(2024, 1, 1));
        box.setEndDate(LocalDate.of(2024, 1, 31));
        box.setRented(true);

        FundraisingEvent event = new FundraisingEvent();
        Account account = new Account();
        Currency currency = new Currency();
        currency.setRateToDollar(BigDecimal.valueOf(1.2));
        account.setChosenCurrency(currency);
        event.setAccount(account);
        box.setFundraisingEvent(event);

        when(boxService.getBoxById(boxId)).thenReturn(box);

        when(boxService.updateBox(eq(boxId), any(Box.class))).thenAnswer(invocation -> invocation.getArgument(1));

        TransferService spyTransferService = Mockito.spy(transferService);

        doReturn(box).when(spyTransferService).transferMoneyToAccount(boxId);

        doNothing().when(spyTransferService).deleteBalances(boxId);

        Box updatedBox = spyTransferService.endRental(boxId);

        assertNull(updatedBox.getStartDate());
        assertNull(updatedBox.getEndDate());
        assertFalse(updatedBox.isRented());
        assertNull(updatedBox.getFundraisingEvent());

        verify(spyTransferService).transferMoneyToAccount(boxId);
        verify(spyTransferService).deleteBalances(boxId);
        verify(boxService).updateBox(eq(boxId), any(Box.class));
    }


    @Test
    void charge_reducesAccountBalanceByBoxPrice() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));

        Currency currency = new Currency();
        currency.setRateToDollar(BigDecimal.ONE);
        account.setChosenCurrency(currency);

        FundraisingEvent fe = new FundraisingEvent();
        fe.setAccount(account);

        Box box = new Box();
        box.setId(1L);
        box.setPrice(BigDecimal.valueOf(20));
        box.setFundraisingEvent(fe);

        box.setStartDate(LocalDate.of(2024, 1, 1));
        box.setEndDate(LocalDate.of(2024, 1, 31));

        when(boxService.getBoxById(box.getId())).thenReturn(box);
        when(donateService.convertFromDollarToCurrency(BigDecimal.valueOf(20), BigDecimal.ONE))
                .thenReturn(BigDecimal.valueOf(20));

        BigDecimal cost = transferService.charge(box.getId());

        assertEquals(BigDecimal.valueOf(20).multiply(BigDecimal.valueOf(30)), cost);
    }
}
