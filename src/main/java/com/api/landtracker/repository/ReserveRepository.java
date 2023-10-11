package com.api.landtracker.repository;

import com.api.landtracker.model.entities.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    @Query("SELECT MAX(c.number) FROM Reserve c")
    String findMaxNumber();

    List<Reserve> findReservesByClientId(Long clientId);

}
