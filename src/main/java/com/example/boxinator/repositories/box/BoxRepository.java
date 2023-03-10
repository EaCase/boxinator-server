package com.example.boxinator.repositories.box;

import com.example.boxinator.models.box.BoxTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BoxRepository extends JpaRepository<BoxTier, Long> {
}
