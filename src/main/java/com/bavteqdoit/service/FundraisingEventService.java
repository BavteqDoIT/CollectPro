package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.FundraisingEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FundraisingEventService {
    private final FundraisingEventRepository fundraisingEventRepository;
    private final AccountService accountService;
    private final OrganizationService organizationService;

    public List<FundraisingEvent> getAllFundraisingEvents() {
        return fundraisingEventRepository.findAll();
    }

    public FundraisingEvent getFundraisingEventById(Long id) {
        return fundraisingEventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fundraising event with id  not found"));
    }

    public FundraisingEvent createFundraisingEvent(Long accountId, Long organizationId, FundraisingEvent fe) {
        Objects.requireNonNull(fe, "FundraisingEvent must not be null");
        if (accountId == null || organizationId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account or organization id must not be null");
        }
        if (accountId <= 0 || organizationId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account or organization id must be greater than 0");
        }
        Account account = accountService.getAccountById(accountId);
        Organization organization = organizationService.getOrganizationById(organizationId);
        if (account.getOrganization().equals(organization)) {
            fe.setAccount(account);
            return fundraisingEventRepository.save(fe);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no account with this id owned by this organization");
    }

    public void deleteFundraisingEvent(Long id) {
        if (id > 0) {
            fundraisingEventRepository.delete(getFundraisingEventById(id));
        }
    }

    public FundraisingEvent updateFundraisingEvent(Long id, FundraisingEvent updatedEvent) {
        Objects.requireNonNull(updatedEvent, "FundraisingEvent must not be null");
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }
        FundraisingEvent existingEvent = fundraisingEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FundraisingEvent not found with id: " + id));

        existingEvent.setName(updatedEvent.getName());
        existingEvent.setEventAddress(updatedEvent.getEventAddress());
        existingEvent.setAccount(updatedEvent.getAccount());

        return fundraisingEventRepository.save(existingEvent);
    }
}
