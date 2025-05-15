package com.bavteqdoit.service;

import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        if (id == null || id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }
        return organizationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Organization not found!"));
    }

    public Organization createOrganization(Organization organization) {
        Objects.requireNonNull(organization, "Organization cannot be null");
        if (organizationRepository.existsByOrganizationName(organization.getOrganizationName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Organization with this name already exists");
        }
        return organizationRepository.save(organization);
    }

    public Organization updateOrganization(Long id, Organization updatedOrganization) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }
        Objects.requireNonNull(updatedOrganization, "Organization cannot be null");
        Organization existingOrganization = getOrganizationById(id);

        existingOrganization.setOwnerFirstName(updatedOrganization.getOwnerFirstName());
        existingOrganization.setOwnerLastName(updatedOrganization.getOwnerLastName());
        existingOrganization.setOwnerEmail(updatedOrganization.getOwnerEmail());
        existingOrganization.setOwnerPhone(updatedOrganization.getOwnerPhone());
        existingOrganization.setOrganizationName(updatedOrganization.getOrganizationName());
        existingOrganization.setOrganizationPhone(updatedOrganization.getOrganizationPhone());
        return organizationRepository.save(existingOrganization);
    }

    public void deleteOrganization(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id must be greater than 0");
        }
        organizationRepository.delete(getOrganizationById(id));
    }
}
