package com.api.landtracker.repository;

import com.api.landtracker.model.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long>, JpaSpecificationExecutor<Lot> {

    List<Lot> findLotsByClientId(Long clientId);
}
