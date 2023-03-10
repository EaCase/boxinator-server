package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.repositories.shipment.ShipmentRepository;
import com.example.boxinator.services.fee.FeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepo;
    private final FeeService feeService;

    public ShipmentServiceImpl(FeeService feeService, ShipmentRepository shipmentRepo) {
        this.feeService = feeService;
        this.shipmentRepo = shipmentRepo;
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
        // TODO
        return feeService.getBaseShipmentFee();
    }

    @Override
    public Shipment create(ShipmentPostDto dto) {
        return null;
    }

    @Override
    public Shipment getById(Long id) {
        return null;
    }

    @Override
    public List<Shipment> getAll() {
        return null;
    }

    @Override
    public Long deleteById(Long id) {
        return null;
    }

    @Override
    public Shipment update(Long id, ShipmentPostDto dto) {
        return null;
    }
}
