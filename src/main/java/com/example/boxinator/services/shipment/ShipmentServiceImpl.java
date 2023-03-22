package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.account.AccountRepository;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.repositories.shipment.ShipmentStatusRepository;
import com.example.boxinator.services.account.AccountService;
import com.example.boxinator.services.box.BoxService;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.fee.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final CountryService countryService;
    private final FeeService feeService;
    private final BoxService boxService;
    private final AccountService accountService;

    private final AccountRepository accountRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentServiceImpl(
            FeeService feeService,
            ShipmentRepository shipmentRepo,
            ShipmentStatusRepository shipmentStatusRepository, ShipmentMapper shipmentMapper,
            CountryService countryService,
            AccountService accountService,
            BoxService boxService,

            AccountRepository accountRepository
    ) {

        this.feeService = feeService;
        this.shipmentRepo = shipmentRepo;
        this.shipmentMapper = shipmentMapper;
        this.countryService = countryService;
        this.accountService = accountService;
        this.boxService = boxService;

        this.shipmentStatusRepository = shipmentStatusRepository;

        this.accountRepository = accountRepository;
    }

    @Override
    public Shipment create(ShipmentPostDto dto) {
        return null;
    }

    @Override
    public List<Shipment> getShipmentsFiltered(Long accountId, Date from, Date to, List<Status> statuses) {
        // Call repo methods based on the filters


        var allStatuses = Arrays.stream(Status.values()).map(Enum::ordinal).toList();

        if (statuses != null && from != null && to != null) {
            // get shipments based on status date from and date to
            return shipmentRepo.findAllByAccountAndDateBetween(accountId, from, to, statuses.stream().map(Enum::ordinal).toList());
        } else if (from != null && to != null) {
            // get shipments based on date range only;
            return shipmentRepo.findAllByAccountAndDateBetween(accountId, from, to, allStatuses);
        } else {
            // get all shipments
            return shipmentRepo.findAllByAccountId(accountId);
        }
    }

    @Override
    public boolean ownsShipment(Long accountId, Long shipmentId) {
        var shipment = shipmentRepo.findById(shipmentId).orElseThrow(() ->
                new ApplicationException("Shipment with the id " + shipmentId + " does not exist.", HttpStatus.BAD_REQUEST)
        );

        return shipment.getAccount().getId().equals(accountId);
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Shipment> getAccountShipments(Long id) {
        return shipmentRepo.findAllByAccountId(id);
    }

    @Override
    public List<Shipment> getByStatus(Long accountId, Status status) {
        var res = shipmentRepo.getShipmentsByStatus(accountId, status.ordinal());
        ;
        return res;
    }

    @Override
    public Fee calculateShipmentCost(Long countryId, Long boxTierId) {
        return feeService.calculateShipmentCost(countryId, boxTierId);
    }

    @Override
    public Shipment createNewShipment(Long accountId, ShipmentPostDto dto) {
        Shipment ship = shipmentMapper.toShipment(dto);
        ship.setAccount(accountService.getById(accountId));
        ship.setCost(feeService.calculateShipmentCost(dto.getCountryId(), dto.getBoxTierId()).getAmount());
        ship.setStatuses(List.of(buildStatus(Status.CREATED, ship)));
        shipmentRepo.save(ship);

        var status = buildStatus(Status.CREATED, ship);
        shipmentStatusRepository.save(status);

        return ship;
    }

    private ShipmentStatus buildStatus(Status status, Shipment shipment) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setTs(new Date(System.currentTimeMillis()));
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }


    @Override
    public List<Shipment> getAll() {
        return shipmentRepo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("Shipment with id" + id + " does not exist.", HttpStatus.BAD_REQUEST));
        shipmentRepo.deleteById(id);
    }

    @Override
    public Shipment updateShipmentStatus(Long id, Status status) {
        Shipment shipment = shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("Shipment with id" + id + " does not exist.", HttpStatus.BAD_REQUEST));

        var shipmentStatus = buildStatus(status, shipment);
        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }

    @Override
    public Shipment update(Long id, ShipmentPostDto dto) {
        Shipment shipment = shipmentRepo.findById(id).orElseThrow(() -> {
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

        return shipmentRepo.save(shipment);
    }
}