package com.bavteqdoit.service;

import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import org.springframework.web.server.ResponseStatusException;

class OrganizationServiceTest {

    private OrganizationRepository organizationRepository;
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        organizationRepository = Mockito.mock(OrganizationRepository.class);
        organizationService = new OrganizationService(organizationRepository);
    }

    @Test
    void getAllOrganizations_shouldReturnList() {
        when(organizationRepository.findAll()).thenReturn(List.of(new Organization(), new Organization()));
        List<Organization> result = organizationService.getAllOrganizations();
        assertEquals(2, result.size());
    }

    @Test
    void getOrganizationById_validId_shouldReturnOrganization() {
        Organization org = new Organization();
        org.setId(1L);
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(org));
        Organization result = organizationService.getOrganizationById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrganizationById_invalidId_shouldThrowBadRequest() {
        assertThrows(ResponseStatusException.class, () -> organizationService.getOrganizationById(0L));
        assertThrows(ResponseStatusException.class, () -> organizationService.getOrganizationById(null));
    }

    @Test
    void getOrganizationById_notFound_shouldThrowNotFound() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> organizationService.getOrganizationById(99L));
        assertThat(exception.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void createOrganization_valid_shouldSave() {
        Organization org = new Organization();
        org.setOrganizationName("Test Org");

        when(organizationRepository.existsByOrganizationName("Test Org")).thenReturn(false);
        when(organizationRepository.save(org)).thenReturn(org);

        Organization result = organizationService.createOrganization(org);
        assertEquals("Test Org", result.getOrganizationName());
    }

    @Test
    void createOrganization_duplicateName_shouldThrowConflict() {
        Organization org = new Organization();
        org.setOrganizationName("Duplicate");

        when(organizationRepository.existsByOrganizationName("Duplicate")).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> organizationService.createOrganization(org));
        assertThat(exception.getStatusCode().value()).isEqualTo(409);
    }

    @Test
    void createOrganization_null_shouldThrow() {
        assertThrows(NullPointerException.class, () -> organizationService.createOrganization(null));
    }

    @Test
    void updateOrganization_valid_shouldUpdateAndSave() {
        Long id = 1L;
        Organization existing = new Organization();
        existing.setId(id);

        Organization updated = new Organization();
        updated.setOwnerFirstName("John");
        updated.setOwnerLastName("Doe");
        updated.setOwnerEmail("john@example.com");
        updated.setOwnerPhone("123456789");
        updated.setOrganizationName("Updated Org");
        updated.setOrganizationPhone("987654321");

        when(organizationRepository.findById(id)).thenReturn(Optional.of(existing));
        when(organizationRepository.save(existing)).thenReturn(existing);

        Organization result = organizationService.updateOrganization(id, updated);

        assertEquals("John", result.getOwnerFirstName());
        assertEquals("Doe", result.getOwnerLastName());
        assertEquals("Updated Org", result.getOrganizationName());
    }

    @Test
    void updateOrganization_null_shouldThrow() {
        assertThrows(NullPointerException.class, () -> organizationService.updateOrganization(1L, null));
    }

    @Test
    void updateOrganization_invalidId_shouldThrow() {
        Organization dummy = new Organization();
        assertThrows(ResponseStatusException.class, () -> organizationService.updateOrganization(0L, dummy));
    }

    @Test
    void deleteOrganization_valid_shouldCallDelete() {
        Long id = 1L;
        Organization org = new Organization();
        org.setId(id);
        when(organizationRepository.findById(id)).thenReturn(Optional.of(org));

        organizationService.deleteOrganization(id);

        verify(organizationRepository, times(1)).delete(org);
    }

    @Test
    void deleteOrganization_invalidId_shouldThrow() {
        assertThrows(ResponseStatusException.class, () -> organizationService.deleteOrganization(0L));
    }

    @Test
    void deleteOrganization_notFound_shouldThrow() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> organizationService.deleteOrganization(1L));
    }
}
