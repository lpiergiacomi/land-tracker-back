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


    @Query(value = "select COUNT(id) as title, 'reserved_lots' as guid from reserve where created_date BETWEEN :startDate AND :endDate AND state != 'VENCIDA' \n" +
            "UNION ALL\n" +
            "select COUNT(id) as title, 'lots_with_reserve_paid' as guid from payment where created_date BETWEEN :startDate AND :endDate AND reason = 'RESERVA'\n" +
            "UNION ALL\n" +
            "select COUNT(id) as title, 'sold_lots' as guid from lot where sale_date BETWEEN :startDate AND :endDate\n" +
            "UNION ALL\n" +
            "select COUNT(id) as title, 'expired_reservations' as guid from reserve where due_date BETWEEN :startDate AND :endDate AND state = 'VENCIDA'\n", nativeQuery = true)
    List<IDashboardCard> getDashboardCardsInfoBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

}
