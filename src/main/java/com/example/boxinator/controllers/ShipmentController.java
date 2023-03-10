package com.example.boxinator.controllers;

import com.example.boxinator.dtos.fee.FeeGetDto;
import com.example.boxinator.dtos.shipment.ShipmentGetDto;
import com.example.boxinator.models.shipment.Status;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "shipments")
public class ShipmentController {

    @GetMapping("/")
    @Operation(summary = "Get all the shipments relevant to the authenticated user. Filterable by status type, or using a date range (from-to).")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipment objects (matching the query params, if any).",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
            )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getAllShipments(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status
    ) {
        /*
        Retrieve a list of shipments relevant to the authenticated user.
        A user will see only their shipments.

        An administrator will see all current (non-cancelled/non-completed) shipments that
        will be displayed on the administrator page.

        This can also optionally be filterable by status type or using a date range (from - to).

        This could be done using query strings to provide these filters
         */
        throw new RuntimeException("Not implemented.");
    }

    @GetMapping("/complete")
    @Operation(summary = "Get all completed shipments relevant to the authenticated user.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipments with the completed status.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
            )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getCompletedShipments() {
        throw new RuntimeException("Not implemented.");
    }

    @GetMapping("/cancelled")
    @Operation(summary = "Get all cancelled shipments relevant to the authenticated user.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipments with the cancelled status.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
            )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getCancelledShipments() {
        throw new RuntimeException("Not implemented.");
    }

    @GetMapping("/cost")
    @Operation(summary = "Calculate a cost to a shipment which has not been sent/created yet, by providing the country id and the box tier id.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns the calculated cost of the shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = FeeGetDto.class)
            )}
    )
    public ResponseEntity<FeeGetDto> calculateShipmentCost(@RequestParam Long countryId, @RequestParam Long boxTierId) {
        // Takes countryId + boxId?
        throw new RuntimeException("Not implemented.");
    }


    @PostMapping("/")
    @Operation(summary = "Create a new shipment with the provided values.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns the created shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentGetDto.class)
            )}
    )
    public ResponseEntity<ShipmentGetDto> createShipment() {
        /*
        Used to create a new shipment, the client data must be retrieved based on the authorization
         transmitted in the request header.

        Administrators can also create shipments.
         */
        throw new RuntimeException("Not implemented.");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a shipment by id.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns the shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentGetDto.class)
            )}
    )
    public ResponseEntity<ShipmentGetDto> getShipmentById(@PathVariable Long id) {
        /*
        Retrieve the details of a single shipment, remember to consider if the current user has
        access the requested shipment. (Users can only view their own shipment)
     */
        throw new RuntimeException("Not implemented.");
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all shipments related to a user (Non-admin users only get their own shipments).")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipments.",
            content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
            )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getAllCustomerShipments(@PathVariable Long customerId) {
        // Non admin users only get their own shipments
        throw new RuntimeException("Not implemented.");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a status of an shipment (non-Administrator users may only cancel a shipment).")
    @ApiResponse(
            responseCode = "200",
            description = "Returns the updated shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentGetDto.class)
            )}
    )
    public ResponseEntity<ShipmentGetDto> updateShipmentStatus(@PathVariable Long id, @RequestParam String status) {
        /*
        This endpoint is used to update a shipment, but any non-Administrator users may
        only cancel a shipment. Meaning, only admins can change shipment statuses aside from
        simply canceling.

        An administrator can make any changes they wish to a shipment. The administrator
        will use this to mark a shipment as completed.
         */
        throw new RuntimeException("Not implemented.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shipment by id (Admin only).")
    @ApiResponse(responseCode = "200",
            description = "Returns the id of the deleted shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Long.class)
            )}
    )
    public ResponseEntity<Long> deleteShipping(@PathVariable Long id) {
        // TODO Admin only
        throw new RuntimeException("Not implemented.");
    }
}
