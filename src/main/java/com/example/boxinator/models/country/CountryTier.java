package com.example.boxinator.models.country;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Contains information related to a Country, e.g. the shipping multiplier used for calculating the costs
 * of an order.
 * <p>
 * There are a few different tiers defined in this implementation. The countries which are used to send
 * the shipments from are in the 'source' tier. In this project, the other countries are more or less randomly set
 * to belong into a certain tier, but the implementation could be improved rather easily (e.g. calculating the distance
 * to the closest 'source' country, and assign the tiers based on it).
 */
@Entity
@Getter
@Setter
public class CountryTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "shipping_multiplier", nullable = false)
    private Float shippingMultiplier;
}
