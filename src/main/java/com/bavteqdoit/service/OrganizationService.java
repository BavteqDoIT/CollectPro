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

    public Organization updateOrganization(long id, Organization organization) {
        Organization oldOrganization = getOrganizationById(id);

        organization.setOwnerFirstName(oldOrganization.getOwnerFirstName());
        organization.setOwnerLastName(oldOrganization.getOwnerLastName());
        organization.setOwnerEmail(oldOrganization.getOwnerEmail());
        organization.setOwnerPhone(oldOrganization.getOwnerPhone());
        organization.setOrganizationName(oldOrganization.getOrganizationName());
        organization.setOrganizationPhone(oldOrganization.getOrganizationPhone());
        return organizationRepository.save(organization);
    }

    public List<Organization> deleteOrganization(long id) {
        Organization organization = getOrganizationById(id);
        organizationRepository.delete(organization);
        return organizationRepository.findAll();
    }
}
