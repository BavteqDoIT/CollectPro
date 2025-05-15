package com.bavteqdoit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean rented = false;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal sum = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private FundraisingEvent fundraisingEvent;
}
