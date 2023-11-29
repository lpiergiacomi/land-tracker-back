package com.api.landtracker.repository;

import com.api.landtracker.model.dto.IPieChartData;
import com.api.landtracker.model.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long>, JpaSpecificationExecutor<Lot> {

    List<Lot> findLotsByClientId(Long clientId);

    @Modifying()
    @Query(value = "DELETE FROM lot_assignment " +
            "WHERE user_id = :userId AND (:hasLots OR lot_id NOT IN (:lotIds))", nativeQuery = true)
    void deleteAssignedLotsByUserId(Long userId, List<Long> lotIds, boolean hasLots);

    List<Lot> findAllByIdIn(Collection<Long> id);

    @Query(value = "SELECT state AS label, COUNT(id) AS data FROM lot GROUP BY state", nativeQuery = true)
    List<IPieChartData> getLotsQuantityByState();

    @Query(value = "SELECT lot_id as id FROM lot_assignment WHERE lot_id = :lotId AND user_id = :userId", nativeQuery = true)
    Optional<Long> findAssignmentLotByUserId(Long lotId, Long userId);
}
