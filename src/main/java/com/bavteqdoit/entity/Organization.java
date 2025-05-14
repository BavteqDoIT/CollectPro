package com.bavteqdoit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {
    @Id
    private long id;

    private String ownerFirstName;

    private String ownerLastName;

    private String ownerEmail;

    private int ownerPhone;

    private String organizationName;

    private int organizationPhone;
}
