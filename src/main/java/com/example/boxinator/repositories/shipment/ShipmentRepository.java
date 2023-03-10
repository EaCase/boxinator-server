package com.example.boxinator.repositories.shipment;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> getShipmentByAccountIdAndStatuses(Long accountId, Status status);

    List<Account> getShipmentsByAccount(Long id);
}
