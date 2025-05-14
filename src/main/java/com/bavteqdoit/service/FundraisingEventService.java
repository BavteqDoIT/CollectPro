package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.FundraisingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final AccountService accountService;
    private final OrganizationService organizationService;

    public FundraisingEvent createFundraisingEvent(Long accountId, Long organizationId, FundraisingEvent fe) {
        Account account = accountService.getAccountById(accountId);
        Organization organization = organizationService.getOrganizationById(organizationId);
        if(account.getOrganization().equals(organization)) {
            fe.setAccount(account);
            return fundraisingEventRepository.save(fe);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with this id owned by this organization");
    }

    public List<FundraisingEvent> getAllFundraisingEvents() {
        return fundraisingEventRepository.findAll();
    }

    public FundraisingEvent getFundraisingEventById(Long id) {
        return fundraisingEventRepository.findById(id).orElse(null);
    }
}
