package com.example.boxinator.repositories.shipment;

import com.example.boxinator.models.account.Account;
import com.example.boxinator.models.shipment.Shipment;
import com.example.boxinator.models.shipment.ShipmentStatus;
import com.example.boxinator.models.shipment.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {


    //    @Query(value =
//            "SELECT account.id, shipment.id, box_tier_id,  FROM account" +
//            "LEFT JOIN shipment on shipment.account_id = account.id" +
//            "WHERE shipment.account_id = ?", nativeQuery = true)
    List<Shipment> findAllByAccountId(Long accountId);

    List<Shipment> findAllByAccount(Account account);


    //List<Shipment> findAllByAccountAndT(Long accountId, Date start, Date end, Status status);

    // List<Shipment> findAllByAccountAndStatus(Account account, Status status);
    @Query(value = "SELECT shipment.shipment_id, shipment.account_id, shipment.box_color, shipment.cost, shipment.recipient, shipment.box_tier_id, shipment.country_id  FROM shipment " +
            "LEFT JOIN shipment_status ON shipment_status.shipment_id = shipment.shipment_id " +
            "LEFT JOIN account ON shipment.account_id = account.account_id " +
            "LEFT JOIN country ON country.country_id = shipment.country_id " +
            "WHERE shipment.account_id = ?1  AND ts BETWEEN ?2 AND ?3 AND shipment_status.status IN ?4", nativeQuery = true)
    List<Shipment> findAllByAccountAndDateBetween(Long accountId, Date startDate, Date endDate, List<Integer> statuses);


    @Query(value = "SELECT shipment.shipment_id, shipment.account_id, shipment.box_color, shipment.cost, shipment.recipient, shipment.box_tier_id, shipment.country_id, shipment_status.status  FROM shipment " +
            "LEFT JOIN (SELECT shipment_id, MAX(ts) as max_ts " +
            "FROM shipment_status "+
            "GROUP BY shipment_id) " +
            "latest_status ON  shipment.shipment_id = latest_status.shipment_id " +
            "LEFT JOIN shipment_status ON shipment_status.shipment_id = latest_status.shipment_id AND shipment_status.ts = latest_status.max_ts " +
            "LEFT JOIN country ON country.country_id = shipment.country_id " +
            "WHERE shipment_status.status IN ?3 AND shipment_status.ts BETWEEN ?1 AND ?2", nativeQuery = true)
    List<Shipment> findAllByDateBetween(Date startDate, Date endDate, List<Integer> statuses);

    @Query(value = "SELECT \n" +
            "shipment.shipment_id, shipment.account_id, shipment.box_color, shipment.cost, shipment.recipient, shipment.box_tier_id, shipment.country_id FROM shipment\n" +
            "LEFT JOIN shipment_status ON shipment_status.shipment_id = shipment.shipment_id\n" +
            "LEFT JOIN account ON shipment.account_id = account.account_id\n" +
            "LEFT JOIN country ON country.country_id = shipment.country_id\n" +
            "WHERE shipment.account_id = ?1 AND shipment_status.status = ?2", nativeQuery = true)
    List<Shipment> getShipmentsByStatus(Long accountId, Integer status);
}