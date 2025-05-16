package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private CurrencyService currencyService;
    private OrganizationService organizationService;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        currencyService = mock(CurrencyService.class);
        organizationService = mock(OrganizationService.class);
        accountService = new AccountService(accountRepository, currencyService, organizationService);
    }

    @Test
    void getAllAccounts() {
        List<Account> mockList = List.of(new Account(), new Account());
        when(accountRepository.findAll()).thenReturn(mockList);

        List<Account> result = accountService.getAllAccounts();
        assertEquals(2, result.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void getAccountById_validId_returnsAccount() {
        Account account = new Account();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountById(1L);
        assertNotNull(result);
        verify(accountRepository).findById(1L);
    }

    @Test
    void getAccountById_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> accountService.getAccountById(0L));
    }

    @Test
    void getAccountById_notFound_throwsException() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> accountService.getAccountById(999L));
    }

    @Test
    void createAccount_validData_returnsSavedAccount() {
        Account inputAccount = new Account();
        Currency currency = new Currency();
        Organization organization = new Organization();

        when(currencyService.getCurrencyByAcronym("USD")).thenReturn(currency);
        when(organizationService.getOrganizationById(1L)).thenReturn(organization);
        when(accountRepository.save(inputAccount)).thenReturn(inputAccount);

        Account result = accountService.createAccount("USD", 1L, inputAccount);

        assertEquals(inputAccount, result);
        assertEquals(organization, inputAccount.getOrganization());
        assertEquals(currency, inputAccount.getChosenCurrency());
    }

    @Test
    void createAccount_nullAccount_throwsException() {
        assertThrows(NullPointerException.class, () -> accountService.createAccount("USD", 1L, null));
    }

    @Test
    void createAccount_blankAcronym_throwsException() {
        assertThrows(ResponseStatusException.class, () -> accountService.createAccount(" ", 1L, new Account()));
    }

    @Test
    void createAccount_invalidOrgId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> accountService.createAccount("USD", 0L, new Account()));
    }

    @Test
    void deleteAccountById_validEmptyAccount_deletesAccount() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.deleteAccountById(1L);
        verify(accountRepository).delete(account);
    }

    @Test
    void deleteAccountById_accountNotEmpty_throwsException() {
        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(ResponseStatusException.class, () -> accountService.deleteAccountById(1L));
    }

    @Test
    void deleteAccountById_invalidId_throwsException() {
        assertThrows(ResponseStatusException.class, () -> accountService.deleteAccountById(0L));
    }

    @Test
    void persistAccount_valid_savesAccount() {
        Account account = new Account();
        accountService.persistAccount(account);
        verify(accountRepository).save(account);
    }

    @Test
    void persistAccount_null_throwsException() {
        assertThrows(NullPointerException.class, () -> accountService.persistAccount(null));
    }
}
