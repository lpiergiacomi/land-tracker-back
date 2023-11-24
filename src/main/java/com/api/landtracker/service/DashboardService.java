package com.api.landtracker.service;

import com.api.landtracker.model.dto.IDashboardCard;
import com.api.landtracker.model.dto.IReserveCalendar;
import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.model.entities.*;
import com.api.landtracker.model.mappers.PaymentMapper;
import com.api.landtracker.repository.DashboardRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DashboardRepository dashboardRepository;
    private final PaymentMapper paymentMapper;

    public List<PaymentDTO> getPaymentsByTimeScale(String timeScale) {

        DatesByTimeScaleWrapper dates = this.getDatesByTimeScale(timeScale);
        return getPaymentsBetweenDates(dates.startDateTime, dates.endDateTime);
    }

    private List<PaymentDTO> getPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = dashboardRepository.findByCreatedDateBetween(startDate, endDate);
        return paymentMapper.paymentsToPaymentsDTO(payments);
    }

    public List<IDashboardCard> getDashboardCardsInfo(String timeScale) {
        DatesByTimeScaleWrapper dates = this.getDatesByTimeScale(timeScale);
        return dashboardRepository.getDashboardCardsInfoBetweenDates(dates.startDateTime, dates.endDateTime);
    }

    private DatesByTimeScaleWrapper getDatesByTimeScale(String timeScale) {

        LocalDateTime startDateTime = LocalDate.now().atStartOfDay();
        LocalDateTime endDateTime = startDateTime.with(LocalTime.MAX);

        switch (timeScale) {
            case "week":
                startDateTime = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                endDateTime = startDateTime.plusDays(6).with(LocalTime.MAX);
                return new DatesByTimeScaleWrapper(startDateTime, endDateTime);
            case "month":
                startDateTime = YearMonth.now().atDay(1).atStartOfDay();
                endDateTime = startDateTime.plusMonths(1).minusDays(1).with(LocalTime.MAX);
                return new DatesByTimeScaleWrapper(startDateTime, endDateTime);
            case "year":
                startDateTime = Year.now().atDay(1).atStartOfDay();
                endDateTime = startDateTime.plusYears(1).minusDays(1).with(LocalTime.MAX);
                return new DatesByTimeScaleWrapper(startDateTime, endDateTime);
            default:
                return new DatesByTimeScaleWrapper(startDateTime, endDateTime);
        }
    }

    public List<IReserveCalendar> getReservesForCalendar(Date startDate, Date endDate) {
        return dashboardRepository.getReservesForCalendar(startDate, endDate);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class DatesByTimeScaleWrapper {
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
    }

}
