package com.bavteqdoit.service;

import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(long id) {
        return organizationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Organization not found!"));
    }

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization updateOrganization(long id, Organization updatedOrganization) {
        Organization existingOrganization = getOrganizationById(id);

        existingOrganization.setOwnerFirstName(updatedOrganization.getOwnerFirstName());
        existingOrganization.setOwnerLastName(updatedOrganization.getOwnerLastName());
        existingOrganization.setOwnerEmail(updatedOrganization.getOwnerEmail());
        existingOrganization.setOwnerPhone(updatedOrganization.getOwnerPhone());
        existingOrganization.setOrganizationName(updatedOrganization.getOrganizationName());
        existingOrganization.setOrganizationPhone(updatedOrganization.getOrganizationPhone());
        return organizationRepository.save(existingOrganization);
    }

    public List<Organization> deleteOrganization(long id) {
        Organization organization = getOrganizationById(id);
        organizationRepository.delete(organization);
        return organizationRepository.findAll();
    }
}
