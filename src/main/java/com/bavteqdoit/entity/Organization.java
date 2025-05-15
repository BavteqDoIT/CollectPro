package com.bavteqdoit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerFirstName;

    @Column(nullable = false)
    private String ownerLastName;

    @Email
    @Column(nullable = false)
    private String ownerEmail;

    @Column(nullable = false)
    private String ownerPhone;

    @Column(nullable = false, unique = true)
    private String organizationName;

    @Column(nullable = false, unique = true)
    private String organizationPhone;


}
