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

    private BigDecimal price;

    private boolean rented;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private FundraisingEvent fundraisingEvent;
}
