package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.account.AccountRepository;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.repositories.shipment.ShipmentStatusRepository;
import com.example.boxinator.services.acoount.AccountService;
import com.example.boxinator.services.box.BoxService;
import com.example.boxinator.services.country.CountryService;
import com.example.boxinator.services.fee.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    private final AccountRepository accountRepository;
    private final ShipmentMapper shipmentMapper;

    public ShipmentServiceImpl(
            FeeService feeService,
            ShipmentRepository shipmentRepo,
            ShipmentStatusRepository shipmentStatusRepository, ShipmentMapper shipmentMapper,
            CountryService countryService,
            AccountService accountService,
            BoxService boxService,
            AccountRepository accountRepository) {
        this.feeService = feeService;
        this.shipmentRepo = shipmentRepo;
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.shipmentMapper = shipmentMapper;
        this.countryService = countryService;
        this.accountService = accountService;
        this.boxService = boxService;

        this.accountRepository = accountRepository;
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Shipment> getAccountShipments(Long id) {

        // WIP
        // DOESNT RETURN CORRECT DATA
        // ACCOUNTREPOSITORY HAS NATIVE QUERY

        return shipmentRepo.findAllByAccountId(id);
    }

    @Override
    public List<Shipment> getByStatus(Long accountId, Status status) {
        // WIP
        // DOESNT GET STATUS
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

        // Maybe not needed since we have above method
        // "createNewStatus"
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public List<Shipment> getAll() {

        return shipmentRepo.findAll();

    }

    @Override
    public void deleteById(Long id) {

        // WIP
        // TODO CAN NOT DELETE SHIPMENT BECAUSE OF CONSTRAINT IN SHIPMENT_STATUS
        // TODO Admin only
        // TODO could not execute statement; SQL [n/a]; constraint [fk8a7kseeww1qq6e6afno34odmk]

        shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("Shipment with this id does not exist.", HttpStatus.NOT_FOUND));

        // Delete all ShipmentStatuses belonging to this shipment id
        // DELETE FROM statustable WHERE shipment_id = {ID}
        // shipmentStatusRepository.deletestatuses(shipmentId)

        shipmentRepo.deleteById(id);
    }

    @Override
    public Shipment updateShipmentStatus(Long id, Status status){
        Shipment shipment = shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with that id", HttpStatus.NOT_FOUND));

        var shipmentStatus = buildStatus(status, shipment);
        shipmentStatusRepository.save(shipmentStatus);

        return shipment;
    }

    @Override
    public Shipment update(Long id, ShipmentPostDto dto) {

        // WIP
        // TODO Figure out how to update Shipment correctly

        if(shipmentRepo.findById(id).isEmpty()) {
            throw new ApplicationException("Could not update shipment with that id", HttpStatus.NOT_FOUND);
        }
        Shipment s = shipmentMapper.toShipment(dto);
        s.setId(id);
        s.setCost(Float.valueOf(s.getCost()));
        s.setBoxColor(dto.getBoxColor());
        s.setRecipient(dto.getRecipient());


        getById(s.getId());
        return shipmentRepo.save(s);
        //s.setStatuses();
        //s.setCountry();


    }
}
