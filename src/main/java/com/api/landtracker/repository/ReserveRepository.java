package com.api.landtracker.repository;

import com.api.landtracker.model.entities.Reserve;
import com.api.landtracker.model.entities.ReserveState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    List<Reserve> findReservesByClientId(Long clientId);

    Reserve findByLotIdAndStateIsNot(Long lot_id, ReserveState state);
}
