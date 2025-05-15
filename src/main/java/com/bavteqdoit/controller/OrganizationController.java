package com.bavteqdoit.controller;

import com.bavteqdoit.entity.Organization;
import com.bavteqdoit.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public Organization getOrganizationById(@PathVariable long id) {
        return organizationService.getOrganizationById(id);
    }

    @PostMapping
    public Organization addOrganization(@Valid @RequestBody Organization organization) {
        return organizationService.createOrganization(organization);
    }

    @PutMapping("/{id}")
    public Organization updateOrganization(@PathVariable long id,
                                           @Valid @RequestBody Organization organization) {
        return organizationService.updateOrganization(id, organization);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

}
