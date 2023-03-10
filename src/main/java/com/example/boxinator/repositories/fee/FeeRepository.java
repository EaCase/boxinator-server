package com.example.boxinator.repositories.fee;

import com.example.boxinator.models.fee.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {

    @Query(value = "SELECT id, name, amount FROM fee WHERE name='Base shipping fee' LIMIT 1", nativeQuery = true)
    Fee getBaseShippingFee();
}
