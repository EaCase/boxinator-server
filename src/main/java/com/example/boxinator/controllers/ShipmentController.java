package com.example.boxinator.controllers;

import com.example.boxinator.dtos.fee.FeeGetDto;
import com.example.boxinator.dtos.fee.FeeMapper;
import com.example.boxinator.dtos.shipment.ShipmentGetDto;
import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.services.account.AccountService;
import com.example.boxinator.services.shipment.ShipmentService;
import com.example.boxinator.services.shipment.ShipmentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.ZoneOffset;
import java.util.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping(value = "shipments")
public class ShipmentController {
    private final ShipmentService shipmentService;
    private final FeeMapper feeMapper;
    private final AccountService accountService;
    private final ShipmentMapper shipmentMapper;
    private final ShipmentRepository shipmentRepository;

    public ShipmentController(
            ShipmentServiceImpl service,
            FeeMapper feeMapper,
            AccountService accountService,
            ShipmentMapper shipmentMapper,
            ShipmentRepository shipmentRepository
    ) {
        this.shipmentService = service;
        this.feeMapper = feeMapper;
        this.accountService = accountService;
        this.shipmentMapper = shipmentMapper;
        this.shipmentRepository = shipmentRepository;
    }


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
            Authentication auth,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String status
    ) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Status> statusEnum = null;
        Date startDate = null;
        Date endDate = null;

        if (from != null) {
            LocalDate localDate = LocalDate.parse(from, formatter);
            Instant instant = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            startDate = Date.from(instant);
        }
        if (to != null) {
            LocalDate localDateTo = LocalDate.parse(to, formatter);
            Instant instantTo = localDateTo.atTime(23, 59, 59).toInstant(ZoneOffset.UTC);
            endDate = Date.from(instantTo);
        }
        if (status != null) {
            statusEnum = List.of(Status.fromString(status));
        }

        var shipments = shipmentService.getShipmentsFiltered(getUserId(auth), startDate, endDate, statusEnum);
        var shipmentDTOs = shipments.stream().map(shipmentMapper::toShipmentDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(shipmentDTOs);
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
    @Operation(summary = "Get all completed shipments relevant to the authenticated user.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipments with the completed status.",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
                    )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getCompletedShipments(Authentication auth) {
        List<Shipment> shipments = shipmentService.getByStatus(getUserId(auth), Status.COMPLETED);
        return ResponseEntity.ok().body(shipments.stream().map(shipmentMapper::toShipmentDto).collect(Collectors.toList()));
    }

    @GetMapping("/cancelled")
    @Operation(summary = "Get all cancelled shipments relevant to the authenticated user.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns a list of the shipments with the cancelled status.",
            content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ShipmentGetDto.class))
                    )}
    )
    public ResponseEntity<List<ShipmentGetDto>> getCancelledShipments(Authentication auth) {
        List<Shipment> shipments = shipmentService.getByStatus(getUserId(auth), Status.CANCELLED);
        return ResponseEntity.ok().body(shipments.stream().map(shipmentMapper::toShipmentDto).collect(Collectors.toList()));
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
        return ResponseEntity.ok().body(feeMapper.toFeeDto(shipmentService.calculateShipmentCost(countryId, boxTierId)));
    }


    @PostMapping("/")
    @Operation(summary = "Create a new shipment with the provided values.")
    @ApiResponse(
            responseCode = "201",
            description = "Returns the created shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentGetDto.class)
            )}
    )
    public ResponseEntity<ShipmentGetDto> createShipment(@RequestBody ShipmentPostDto body, Authentication auth) {
        Shipment shipment = shipmentService.createNewShipment(getUserId(auth), body);
        URI location = URI.create("shipments/" + shipment.getId());
        return ResponseEntity.created(location).body(shipmentMapper.toShipmentDto(shipment));
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
        // TODO Access own shipments only
        return ResponseEntity.ok().body(shipmentMapper.toShipmentDto(shipmentService.getById(id)));
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
        // TODO Non admin users only get their own shipments
        List<ShipmentGetDto> shipments = shipmentService.getAccountShipments(customerId).stream().map(shipmentMapper::toShipmentDto).toList();
        return ResponseEntity.ok().body(shipments);
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
    public ResponseEntity<ShipmentGetDto> updateShipmentStatus(@PathVariable Long id, @RequestBody String status) {
        /*
        This endpoint is used to update a shipment, but any non-Administrator users may
        only cancel a shipment. Meaning, only admins can change shipment statuses aside from
        simply canceling.

        An administrator can make any changes they wish to a shipment. The administrator
        will use this to mark a shipment as completed.
         */
        // TODO Admin checks
        Status statusEnum = Status.fromString(status);
        var shipment = shipmentService.updateShipmentStatus(id, statusEnum);
        return ResponseEntity.ok().body(shipmentMapper.toShipmentDto(shipment));
    }


    @PutMapping("/update/{id}")
    @Operation(summary = "Update an entire shipment.")
    @ApiResponse(
            responseCode = "200",
            description = "Returns the updated shipment.",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ShipmentGetDto.class)
            )}
    )
    public ResponseEntity<ShipmentGetDto> updateShipment(@PathVariable Long id, @RequestBody ShipmentPostDto shipmentPost) {
        // TODO Ownership checks
        return ResponseEntity.ok().body(shipmentMapper.toShipmentDto(shipmentService.update(id, shipmentPost)));
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
        shipmentService.deleteById(id);
        return ResponseEntity.ok().body(id);
    }

    private Long getUserId(Authentication auth) {
        var jwt = (Jwt) auth.getPrincipal();
        Account account = accountService.getByProviderId(jwt.getSubject());
        return account.getId();
    }
}
