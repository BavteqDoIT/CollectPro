package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.Currency;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid organization id");
        }
        return accountRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public Account createAccount(String acronym, long organizationId, Account account) {
        if (acronym == null || acronym.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency acronym is required");
        }

        Objects.requireNonNull(account, "Account cannot be null");

        if (organizationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid organization id");
        }

        Currency currency = currencyService.getCurrencyByAcronym(acronym);
        Organization organization = organizationService.getOrganizationById(organizationId);

        account.setOrganization(organization);
        account.setChosenCurrency(currency);

        return accountRepository.save(account);
    }

    public void deleteAccountById(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account id");
        }
        Account account = getAccountById(id);
        if (account.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            accountRepository.delete(account);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account cannot be deleted because it's not empty");
        }
    }

    public void persistAccount(Account account) {
        Objects.requireNonNull(account, "Account cannot be null");
        accountRepository.save(account);
    }
}
