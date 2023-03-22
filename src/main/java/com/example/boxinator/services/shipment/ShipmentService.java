package com.example.boxinator.services.shipment;

import com.example.boxinator.dtos.shipment.ShipmentPostDto;
import com.example.boxinator.models.fee.Fee;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.Status;
import com.example.boxinator.services.shared.CrudService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface ShipmentService extends CrudService<Shipment, ShipmentPostDto> {
    List<Shipment> getAccountShipments(Long id);

    List<Shipment> getByStatus(Long accountId, Status status);

    Fee calculateShipmentCost(Long countryId, Long boxTierId);

    Shipment createNewShipment(Long accountId, ShipmentPostDto dto);

    Shipment updateShipmentStatus(Long id, Status status);

    List<Shipment> getShipmentsFiltered(Long accountId, Date from, Date to, List<Status> statuses);

    boolean ownsShipment(Long accountId, Long shipmentId);
}