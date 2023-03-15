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
    @Column(name = "box_tier_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private Float weight;
}
