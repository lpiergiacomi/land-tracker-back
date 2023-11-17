package com.api.landtracker.repository;

import com.api.landtracker.model.dto.IDashboardCard;
import com.api.landtracker.model.entities.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<Payment, Long>{

    @EntityGraph(attributePaths = {"client", "user", "file"})
    List<Payment> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);


    @Query(value = "select COUNT(id) as title, 'lotes_reservados_en_el_mes' as guid from reserve where created_date BETWEEN :startDate AND :endDate \n" +
            "UNION ALL\n" +
            "select COUNT(id) as title, 'lotes_con_reserva_abonada_en_el_mes' as guid from payment where created_date BETWEEN :startDate AND :endDate AND reason = 'RESERVA'\n" +
            "UNION ALL\n" +
            "select COUNT(id) as title, 'lotes_vendidos_en_el_mes' as guid from lot where sale_date BETWEEN :startDate AND :endDate\n", nativeQuery = true)
    List<IDashboardCard> getDashboardCardsInfoBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

}
