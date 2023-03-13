package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShipmentService extends CrudService<Shipment, ShipmentPostDto> {
    List<Account> getAccountShipments(Long id);

    List<Shipment> getByStatus(Long accountId, Status status);

    Fee calculateShipmentCost(Long countryId, Long boxTierId);

    Shipment createNewShipment(Long accountId, ShipmentPostDto dto);



}
