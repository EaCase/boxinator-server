package com.example.boxinator.models.shipment;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.box.BoxTier;
import com.example.boxinator.models.country.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Shipments made by the user, containing a box which is to be 'delivered' to the user.
 */
@Entity
@Getter
@Setter
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(optional = false)
    @JoinColumn(name = "box_tier_id")
    private BoxTier boxTier;

    @Column(nullable = false, length = 10)
    private String boxColor;

    @Column(nullable = false)
    private Float cost;

    @Column(nullable = false)
    private String recipient;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "shipment")
    private List<ShipmentStatus> statuses;
}
