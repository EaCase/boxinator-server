package com.example.boxinator.models.shipment;

import com.example.boxinator.errors.exceptions.ApplicationException;
import org.springframework.http.HttpStatus;

/**
 * Shipment statuses.
 */
public enum Status {
    CREATED,
    RECEIVED,
    INTRANSIT,
    COMPLETED,
    CANCELLED;

    public static Status fromString(String string) {
        return switch(string) {
            case "CREATED" -> Status.CREATED;
            case "RECEIVED" -> Status.RECEIVED;
            case "INTRANSIT" -> Status.INTRANSIT;
            case "COMPLETED" -> Status.COMPLETED;
            case "CANCELLED" -> Status.CANCELLED;

            default -> throw new ApplicationException("That status does not exist", HttpStatus.NOT_FOUND);
        };
    }
}
