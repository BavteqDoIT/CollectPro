package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;
    private final OrganizationService organizationService;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account createAccount( String acronym, long organizationId, Account account) {
        Currency currency = currencyService.getCurrencyByAcronym(acronym);
        Organization organization = organizationService.getOrganizationById(organizationId);
        account.setOrganization(organization);
        account.setChosenCurrency(currency);


        return accountRepository.save(account);
    }

    public List<Account> deleteAccountById(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
        return accountRepository.findAll();
    }

    public Account updateAccountById(Long id) {
        Account existingAccount = getAccountById(id);
        return accountRepository.save(existingAccount);
    }
}
