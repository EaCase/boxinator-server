package com.example.boxinator.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "shipments")
public class ShipmentController {

    @GetMapping("/")
    public void getAllShipments() {
        /*
        Retrieve a list of shipments relevant to the authenticated user.
        A user will see only their shipments.

        An administrator will see all current (non-cancelled/non-completed) shipments that
        will be displayed on the administrator page.

        This can also optionally be filterable by status type or using a date range (from - to).

        This could be done using query strings to provide these filters
         */
    }

    @GetMapping("/complete")
    public void getCompletedShipments() {
    }

    @GetMapping("/cost")
    public void calculateShipmentCost() {
        // Takes countryId + boxId?
    }

    @GetMapping("/cancelled")
    public void getCancelledShipments() {
    }

    @PostMapping("/")
    public void createShipment() {
        /*
        Used to create a new shipment, the client data must be retrieved based on the authorization
         transmitted in the request header.

        Administrators can also create shipments.
         */
    }

    @GetMapping("/{id}")
    public void getShipmentById(@PathVariable Long id) {
        /*
        Retrieve the details of a single shipment, remember to consider if the current user has
        access the requested shipment. (Users can only view their own shipment)
     */
    }

    @GetMapping("/customer/{customerId}")
    public void getAllCustomerShipments(@PathVariable Long customerId) {
        // Non admin users only get their own shipments
    }

    @PutMapping("/{id}")
    public void updateShipmentStatus(@PathVariable Long id) {
        /*
        This endpoint is used to update a shipment, but any non-Administrator users may
        only cancel a shipment. Meaning, only admins can change shipment statuses aside from
        simply canceling.

        An administrator can make any changes they wish to a shipment. The administrator
        will use this to mark a shipment as completed.
         */
    }

    @DeleteMapping("/{id}")
    public void deleteShipping(@PathVariable Long id) {
        // TODO Admin only
    }

}
