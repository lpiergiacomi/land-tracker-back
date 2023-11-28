package com.api.landtracker.repository;

import com.api.landtracker.model.dto.IDashboardCard;
import com.api.landtracker.model.dto.IReserveCalendar;
import com.api.landtracker.model.entities.Payment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
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

    @Query(value = "select r.due_date as date, l.name as title, l.id as lotId, r.id as reserveId \n" +
            "from reserve r \n" +
            "left join lot l on l.id = r.lot \n" +
            "inner join lot_assignment la on la.lot_id = l.id \n" +
            "where r.due_date BETWEEN :startDate AND :endDate \n" +
            "and r.state = 'PENDIENTE_DE_PAGO' and la.user_id = :userId", nativeQuery = true)
    List<IReserveCalendar> getReservesForCalendar(Date startDate, Date endDate, Long userId);
}
