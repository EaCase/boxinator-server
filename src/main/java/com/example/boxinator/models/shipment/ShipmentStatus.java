package com.example.boxinator.models.shipment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Status of an shipment.
 */
@Entity
@Getter
@Setter
public class ShipmentStatus {
    @Id
    @Column(name = "shipment_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    @Column
    private Date ts;
}
