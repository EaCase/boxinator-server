package com.example.boxinator.models.box;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Representation for the different mystery box tiers available for order.
 */
@Entity
@Getter
@Setter
public class BoxTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private Float weight;
}
