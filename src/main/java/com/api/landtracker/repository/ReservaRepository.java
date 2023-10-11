package com.api.landtracker.repository;

import com.api.landtracker.model.entities.Lote;
import com.api.landtracker.model.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT MAX(c.numero) FROM Reserva c")
    String findMaxNumero();

    List<Reserva> findReservasByClienteId(Long cliente_id);

}
