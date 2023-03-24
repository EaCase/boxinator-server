package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.repositories.shipment.ShipmentStatusRepository;
import com.example.boxinator.services.account.AccountService;
import com.example.boxinator.services.box.BoxService;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.email.EmailService;
import com.example.boxinator.services.fee.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final CountryService countryService;
    private final FeeService feeService;
    private final BoxService boxService;
    private final AccountService accountService;
    private final ShipmentMapper shipmentMapper;
    private final EmailService emailService;

    public ShipmentServiceImpl(
            FeeService feeService,
            ShipmentRepository shipmentRepository,
            ShipmentStatusRepository shipmentStatusRepository, ShipmentMapper shipmentMapper,
            CountryService countryService,
            AccountService accountService,
            BoxService boxService,
            EmailService emailService) {
        this.feeService = feeService;
        this.shipmentRepository = shipmentRepository;
        this.shipmentMapper = shipmentMapper;
        this.countryService = countryService;
        this.accountService = accountService;
        this.boxService = boxService;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.emailService = emailService;
    }

    @Override
    public Shipment create(ShipmentPostDto dto) {
        throw new ApplicationException("Not implemented.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<Shipment> getShipmentsFiltered(Long accountId, Date from, Date to, List<Status> statuses) {
        var allStatuses = Arrays.stream(Status.values()).map(Enum::ordinal).toList();

        if(accountId == null && from != null && to != null) {
            var shipmentStatuses = List.of(Status.INTRANSIT, Status.CREATED, Status.RECEIVED);
            // Ability to see all shipments that are not cancelled or complete !NB Admin only!
            return shipmentRepository.findAllByDateBetween(from, to, shipmentStatuses.stream().map(Enum::ordinal).toList());
        }
        else if (statuses != null && from != null && to != null) {
            // get shipments based on status date from and date to
            return shipmentRepository.findAllByAccountAndDateBetween(accountId, from, to, statuses.stream().map(Enum::ordinal).toList());
        } else if (from != null && to != null) {
            // get shipments based on date range only;
            return shipmentRepository.findAllByAccountAndDateBetween(accountId, from, to, allStatuses);
        } else {
            // get all shipments
            return shipmentRepository.findAllByAccountId(accountId);
        }
    }

    @Override
    public boolean ownsShipment(Long accountId, Long shipmentId) {
        var shipment = shipmentRepository.findById(shipmentId).orElseThrow(() ->
                new ApplicationException("Shipment with the id " + shipmentId + " does not exist.", HttpStatus.BAD_REQUEST)
        );

        return shipment.getAccount().getId().equals(accountId);
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Shipment> getAccountShipments(Long id) {
        return shipmentRepository.findAllByAccountId(id);
    }

    @Override
    public List<Shipment> getByStatus(Long accountId, Status status) {
        return shipmentRepository.getShipmentsByStatus(accountId, status.ordinal());
    }

    @Override
    public Fee calculateShipmentCost(Long countryId, Long boxTierId) {
        return feeService.calculateShipmentCost(countryId, boxTierId);
    }

    @Override
    public Shipment orderShipmentWithAccountId(Long accountId, ShipmentPostDto dto) {
        Shipment shipment = buildShipment(accountId, dto);
        ShipmentStatus status = buildStatus(Status.CREATED, shipment);

        shipmentRepository.save(shipment);
        shipmentStatusRepository.save(status);

        emailService.sendOrderConfirmation(accountService.getById(accountId).getEmail(),shipment.getId());

        return shipment;
    }

    private Shipment buildShipment(Long accountId, ShipmentPostDto dto) {
        Shipment shipment = shipmentMapper.toShipment(dto);
        shipment.setAccount(accountService.getById(accountId));
        shipment.setCost(feeService.calculateShipmentCost(dto.getCountryId(), dto.getBoxTierId()).getAmount());
        shipment.setStatuses(List.of(buildStatus(Status.CREATED, shipment)));
        return shipment;
    }

    private ShipmentStatus buildStatus(Status status, Shipment shipment) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setTs(new Date(System.currentTimeMillis()));
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }

    @Override
    public Shipment orderShipmentWithEmail(String email, ShipmentPostDto dto) {
        AccountService.AccountStatus status = accountService.getAccountStatus(email);
        Account temporaryAccount = switch (status) {
            case DOES_NOT_EXIST -> accountService.registerTempAccount(email);
            case TEMPORARY -> accountService.getByEmail(email);
            case REGISTERED ->
                    throw new ApplicationException("Registered users can't order shipments without logging in.", HttpStatus.BAD_REQUEST);
        };

        return this.orderShipmentWithAccountId(temporaryAccount.getId(), dto);
    }

    @Override
    public List<Shipment> getAll() {
        return shipmentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        shipmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Shipment with id" + id + " does not exist.", HttpStatus.BAD_REQUEST));
        shipmentRepository.deleteById(id);
    }

    @Override
    public Shipment updateShipmentStatus(Long id, Status status) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Shipment with id" + id + " does not exist.", HttpStatus.BAD_REQUEST));

        var shipmentStatus = buildStatus(status, shipment);
        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }

    @Override
    public Shipment update(Long id, ShipmentPostDto dto) {
        Shipment shipment = shipmentRepository.findById(id).orElseThrow(() -> {
            throw new ApplicationException("Shipment with id " + id + " does not exist.", HttpStatus.BAD_REQUEST);
        });

        if (dto.getRecipient() != null) {
            shipment.setRecipient(dto.getRecipient());
        }

        if (dto.getBoxColor() != null) {
            shipment.setBoxColor(dto.getBoxColor());
        }

        if (dto.getBoxTierId() != null) {
            var boxTier = boxService.getById(dto.getBoxTierId());
            shipment.setBoxTier(boxTier);
        }

        if (dto.getCountryId() != null) {
            var countryId = countryService.getById(dto.getCountryId());
            shipment.setCountry(countryId);
        }


        return shipmentRepository.save(shipment);

    }
}