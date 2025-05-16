package com.bavteqdoit.service;

import com.bavteqdoit.entity.Account;
import com.bavteqdoit.entity.FundraisingEvent;
import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.FundraisingEventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundraisingEventServiceTest {

    private FundraisingEventRepository fundraisingEventRepository;
    private AccountService accountService;
    private OrganizationService organizationService;
    private FundraisingEventService fundraisingEventService;

    @BeforeEach
    void setUp() {
        fundraisingEventRepository = mock(FundraisingEventRepository.class);
        accountService = mock(AccountService.class);
        organizationService = mock(OrganizationService.class);
        fundraisingEventService = new FundraisingEventService(fundraisingEventRepository, accountService, organizationService);
    }

    @Test
    void getAllFundraisingEvents_shouldReturnList() {
        List<FundraisingEvent> events = List.of(new FundraisingEvent(), new FundraisingEvent());
        when(fundraisingEventRepository.findAll()).thenReturn(events);

        List<FundraisingEvent> result = fundraisingEventService.getAllFundraisingEvents();

        assertEquals(2, result.size());
        verify(fundraisingEventRepository).findAll();
    }

    @Test
    void getFundraisingEventById_validId_shouldReturnEvent() {
        FundraisingEvent event = new FundraisingEvent();
        when(fundraisingEventRepository.findById(1L)).thenReturn(Optional.of(event));

        FundraisingEvent result = fundraisingEventService.getFundraisingEventById(1L);

        assertNotNull(result);
        verify(fundraisingEventRepository).findById(1L);
    }

    @Test
    void getFundraisingEventById_invalidId_shouldThrow() {
        when(fundraisingEventRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> fundraisingEventService.getFundraisingEventById(99L));
    }

    @Test
    void createFundraisingEvent_valid_shouldSaveAndReturn() {
        Long accountId = 1L;
        Long organizationId = 2L;

        Account account = new Account();
        Organization organization = new Organization();
        FundraisingEvent event = new FundraisingEvent();

        account.setOrganization(organization);

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(organizationService.getOrganizationById(organizationId)).thenReturn(organization);
        when(fundraisingEventRepository.save(event)).thenReturn(event);

        FundraisingEvent result = fundraisingEventService.createFundraisingEvent(accountId, organizationId, event);

        assertEquals(event, result);
        verify(fundraisingEventRepository).save(event);
    }

    @Test
    void createFundraisingEvent_invalidAccount_shouldThrow() {
        Long accountId = 1L;
        Long organizationId = 2L;

        Organization org1 = new Organization();
        org1.setId(1L);

        Organization org2 = new Organization();
        org2.setId(2L);

        Account account = new Account();
        account.setOrganization(org1);

        FundraisingEvent event = new FundraisingEvent();

        when(accountService.getAccountById(accountId)).thenReturn(account);
        when(organizationService.getOrganizationById(organizationId)).thenReturn(org2);

        assertThrows(ResponseStatusException.class, () ->
                fundraisingEventService.createFundraisingEvent(accountId, organizationId, event));
    }

    @Test
    void deleteFundraisingEvent_validId_shouldDelete() {
        FundraisingEvent event = new FundraisingEvent();
        when(fundraisingEventRepository.findById(1L)).thenReturn(Optional.of(event));

        fundraisingEventService.deleteFundraisingEvent(1L);

        verify(fundraisingEventRepository).delete(event);
    }

    @Test
    void deleteFundraisingEvent_invalidId_shouldDoNothing() {
        // nie powinno rzucać wyjątku, po prostu nic nie robi
        fundraisingEventService.deleteFundraisingEvent(-1L);
        verify(fundraisingEventRepository, never()).delete(any());
    }

    @Test
    void updateFundraisingEvent_valid_shouldUpdateFields() {
        Long id = 1L;
        FundraisingEvent existing = new FundraisingEvent();
        FundraisingEvent updated = new FundraisingEvent();
        updated.setName("New Name");
        updated.setEventAddress("New Address");
        updated.setAccount(new Account());

        when(fundraisingEventRepository.findById(id)).thenReturn(Optional.of(existing));
        when(fundraisingEventRepository.save(existing)).thenReturn(existing);

        FundraisingEvent result = fundraisingEventService.updateFundraisingEvent(id, updated);

        assertEquals("New Name", result.getName());
        assertEquals("New Address", result.getEventAddress());
        assertEquals(updated.getAccount(), result.getAccount());
        verify(fundraisingEventRepository).save(existing);
    }

    @Test
    void updateFundraisingEvent_invalidId_shouldThrow() {
        when(fundraisingEventRepository.findById(999L)).thenReturn(Optional.empty());

        FundraisingEvent updated = new FundraisingEvent();

        assertThrows(EntityNotFoundException.class, () -> fundraisingEventService.updateFundraisingEvent(999L, updated));
    }

    @Test
    void updateFundraisingEvent_nullObject_shouldThrow() {
        assertThrows(NullPointerException.class, () -> fundraisingEventService.updateFundraisingEvent(1L, null));
    }

    @Test
    void createFundraisingEvent_nullObject_shouldThrow() {
        assertThrows(NullPointerException.class, () -> fundraisingEventService.createFundraisingEvent(1L, 1L, null));
    }

    @Test
    void createFundraisingEvent_invalidId_shouldThrow() {
        FundraisingEvent fe = new FundraisingEvent();
        assertThrows(ResponseStatusException.class, () -> fundraisingEventService.createFundraisingEvent(null, 1L, fe));
        assertThrows(ResponseStatusException.class, () -> fundraisingEventService.createFundraisingEvent(1L, null, fe));
        assertThrows(ResponseStatusException.class, () -> fundraisingEventService.createFundraisingEvent(0L, 1L, fe));
    }
}
