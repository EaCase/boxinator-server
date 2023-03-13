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
import com.example.boxinator.services.acoount.AccountService;
import com.example.boxinator.services.box.BoxService;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.fee.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final ShipmentStatusRepository shipmentStatusRepository;
    private final CountryService countryService;
    private final FeeService feeService;
    private final BoxService boxService;
    private final AccountService accountService;

    private final ShipmentMapper shipmentMapper;

    public ShipmentServiceImpl(
            FeeService feeService,
            ShipmentRepository shipmentRepo,
            ShipmentStatusRepository shipmentStatusRepository, ShipmentMapper shipmentMapper,
            CountryService countryService,
            AccountService accountService,
            BoxService boxService
            ) {
        this.feeService = feeService;
        this.shipmentRepo = shipmentRepo;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.shipmentMapper = shipmentMapper;
        this.countryService = countryService;
        this.accountService = accountService;
        this.boxService = boxService;

    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Account> getAccountShipments(Long id) {
        return null;
    }

    @Override
    public List<Shipment> getByStatus(Long accountId, Status status) {
        return shipmentRepo.getShipmentByAccountIdAndStatuses(accountId, status);
    }

    @Override
    public Fee calculateShipmentCost(Long countryId, Long boxTierId) {
        return feeService.calculateShipmentCost(countryId, boxTierId);
    }

    @Override
    public Shipment createNewShipment(Long accountId, ShipmentPostDto dto) {
        Shipment ship = shipmentMapper.toShipment(dto);
        ship.setAccount(accountService.getById(accountId));
        ship.setCost(feeService.calculateShipmentCost(dto.getCountryId(),dto.getBoxTierId()).getAmount());
        shipmentRepo.save(ship);

        var status = buildStatus(Status.CREATED, ship);
        shipmentStatusRepository.save(status);

        ship.setStatuses(List.of(buildStatus(Status.CREATED, ship)));
        return ship;
    }

    private ShipmentStatus buildStatus(Status status, Shipment shipment) {
        ShipmentStatus shipmentStatus = new ShipmentStatus();
        shipmentStatus.setTs(new Timestamp(System.currentTimeMillis()));
        shipmentStatus.setShipment(shipment);
        shipmentStatus.setStatus(status);
        return shipmentStatus;
    }

    @Override
    public Shipment create(ShipmentPostDto dto) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<Shipment> getAll() {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Long deleteById(Long id) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Shipment update(Long id, ShipmentPostDto dto) {
        throw new RuntimeException("Not implemented.");
    }
}
