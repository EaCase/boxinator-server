package com.example.boxinator.repositories.shipment;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {


//    @Query(value =
//            "SELECT account.id, shipment.id, box_tier_id,  FROM account" +
//            "LEFT JOIN shipment on shipment.account_id = account.id" +
//            "WHERE shipment.account_id = ?", nativeQuery = true)
    List<Shipment> findAllByAccountId(Long accountId);

    List<Shipment> getShipmentByAccountIdAndStatuses(Long accountId, Status status);


    //@Query(value = "", nativeQuery = true)
//    List<Shipment> findAllByShipmentStatusEquals

    List<Account> getShipmentsByAccount(Long id);
}
