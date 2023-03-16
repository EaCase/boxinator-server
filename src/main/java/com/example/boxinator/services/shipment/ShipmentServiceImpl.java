package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentMapper;
import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.errors.exceptions.ApplicationException;
import com.example.boxinator.models.country.Country;
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
import java.util.Optional;
import java.util.Set;

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
    public Shipment create(ShipmentPostDto dto) {
        return null;
    }

    @Override
    public Shipment getById(Long id) {
        return shipmentRepo.findById(id)
                .orElseThrow(() -> new ApplicationException("No shipment with id: " + id, HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Shipment> getAccountShipments(Long id) {

        // ACCOUNTREPOSITORY HAS NATIVE QUERY

        return shipmentRepo.findAllByAccountId(id);
    }

    @Override
    public List<Shipment> getByStatus(Long accountId, Status status) {
        var res = shipmentRepo.getShipmentsByStatus(accountId, status.ordinal());
        System.out.println(res);
        System.out.println(res.size());
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
    public List<Shipment> getAll() {

        return shipmentRepo.findAll();

    }

    @Override
    public void deleteById(Long id) {


        // TODO Admin only

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

       Optional <Shipment> optionalShipment = shipmentRepo.findById(id);


        if(optionalShipment.isEmpty()) {
            throw new ApplicationException("Could not update shipment with that id", HttpStatus.NOT_FOUND);
        }

        Shipment shipment = optionalShipment.get();

        if(dto.getRecipient() != null) {
            shipment.setRecipient(dto.getRecipient());
        }
        if(dto.getBoxColor() != null) {
            shipment.setBoxColor(dto.getBoxColor());
        }

        if(dto.getBoxTierId() !=null) {
            var boxTier = boxService.getById(dto.getBoxTierId());
            shipment.setBoxTier(boxTier);
        }

        if(dto.getCountryId() != null) {
            var countryId = countryService.getById(dto.getCountryId());
            shipment.setCountry(countryId);
        }

        // Add more update methods if needed



        return shipmentRepo.save(shipment);


    }
}
