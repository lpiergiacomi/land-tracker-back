package com.api.landtracker.controller;

import com.api.landtracker.model.dto.IDashboardCard;
import com.api.landtracker.model.dto.PaymentChartDTO;
import com.api.landtracker.model.dto.PaymentDTO;
import com.api.landtracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {


    private final DashboardService dashboardService;

    @GetMapping("/charts-info/{timeScale}")
    public List<PaymentChartDTO> getPaymentChartData(@PathVariable String timeScale) {
        List<PaymentDTO> payments = dashboardService.getPaymentsByTimeScale(timeScale);
        return convertPaymentsToChartFormat(payments, timeScale);
    }

    @GetMapping("/cards-info/{timeScale}")
    public List<IDashboardCard> getDashboardCardsInfo(@PathVariable String timeScale) {
        return dashboardService.getDashboardCardsInfo(timeScale);
    }

    private List<PaymentChartDTO> convertPaymentsToChartFormat(List<PaymentDTO> payments, String timeScale) {
        List<PaymentChartDTO> chartData = new ArrayList<>();

        if (timeScale.equals("day")) {
            Map<String, BigDecimal> paymentsByHour = groupPaymentsByHour(payments);
            for (Map.Entry<String, BigDecimal> entry : paymentsByHour.entrySet()) {
                chartData.add(new PaymentChartDTO(entry.getKey(), entry.getValue()));
            }
        }
        if (timeScale.equals("week")) {
            Map<String, BigDecimal> paymentsByDayOfWeek = groupPaymentsByDayOfWeek(payments);
            for (Map.Entry<String, BigDecimal> entry : paymentsByDayOfWeek.entrySet()) {
                chartData.add(new PaymentChartDTO(entry.getKey(), entry.getValue()));
            }
        }
        if (timeScale.equals("month")) {
            Map<String, BigDecimal> paymentsByDayOfMonth = groupPaymentsByDayOfMonth(payments);
            for (Map.Entry<String, BigDecimal> entry : paymentsByDayOfMonth.entrySet()) {
                chartData.add(new PaymentChartDTO(entry.getKey(), entry.getValue()));
            }
        }
        if (timeScale.equals("year")) {
            Map<String, BigDecimal> paymentsByMonthOfYear = groupPaymentsByMonthOfYear(payments);
            for (Map.Entry<String, BigDecimal> entry : paymentsByMonthOfYear.entrySet()) {
                chartData.add(new PaymentChartDTO(entry.getKey(), entry.getValue()));
            }
        }

        return chartData;
    }

    public Map<String, BigDecimal> groupPaymentsByHour(List<PaymentDTO> payments) {
        Map<String, BigDecimal> paymentsByHour = initializeWithZeroHours();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'hs'");

        for (PaymentDTO payment : payments) {
            String hour = payment.getCreatedDate().format(formatter);
            BigDecimal amount = payment.getAmount();
            paymentsByHour.merge(hour, amount, BigDecimal::add);
        }

        return paymentsByHour;
    }

    private Map<String, BigDecimal> initializeWithZeroHours() {
        Map<String, BigDecimal> hoursMap = new LinkedHashMap<>();
        LocalDateTime currentDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'hs'");
        for (int hour = 0; hour < 24; hour++) {
            String formattedHour = currentDateTime.withHour(hour).format(formatter);
            hoursMap.put(formattedHour, BigDecimal.ZERO);
        }

        return hoursMap;
    }

    public Map<String, BigDecimal> groupPaymentsByDayOfWeek(List<PaymentDTO> payments) {
        Map<String, BigDecimal> paymentsByDayOfWeek = initializeWithZeroDaysOfWeek();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

        for (PaymentDTO payment : payments) {
            String dayOfWeek = payment.getCreatedDate().format(formatter);
            BigDecimal amount = payment.getAmount();
            paymentsByDayOfWeek.merge(dayOfWeek, amount, BigDecimal::add);
        }

        return paymentsByDayOfWeek;
    }

    private Map<String, BigDecimal> initializeWithZeroDaysOfWeek() {
        Map<String, BigDecimal> daysOfWeekMap = new LinkedHashMap<>();
        LocalDate currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        for (int day = 0; day < 7; day++) {
            String dayOfWeek = currentWeekStart.plusDays(day).format(formatter);
            daysOfWeekMap.put(dayOfWeek, BigDecimal.ZERO);
        }

        return daysOfWeekMap;
    }

    public Map<String, BigDecimal> groupPaymentsByDayOfMonth(List<PaymentDTO> payments) {
        Map<String, BigDecimal> paymentsByDayOfMonth = initializeWithZeroDaysOfMonth();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

        for (PaymentDTO payment : payments) {
            String dayOfMonth = payment.getCreatedDate().format(formatter);
            BigDecimal amount = payment.getAmount();
            paymentsByDayOfMonth.merge(dayOfMonth, amount, BigDecimal::add);
        }

        return paymentsByDayOfMonth;
    }

    private Map<String, BigDecimal> initializeWithZeroDaysOfMonth() {
        Map<String, BigDecimal> daysOfMonthMap = new LinkedHashMap<>();
        LocalDate currentMonthStart = LocalDate.now().withDayOfMonth(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        int daysInMonth = currentMonthStart.lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            String dayOfMonth = currentMonthStart.withDayOfMonth(day).format(formatter);
            daysOfMonthMap.put(dayOfMonth, BigDecimal.ZERO);
        }

        return daysOfMonthMap;
    }

    public Map<String, BigDecimal> groupPaymentsByMonthOfYear(List<PaymentDTO> payments) {
        Map<String, BigDecimal> paymentsByMonthOfYear = initializeWithZeroMonthsOfYear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");

        for (PaymentDTO payment : payments) {
            String monthOfYear = payment.getCreatedDate().format(formatter);
            BigDecimal amount = payment.getAmount();
            paymentsByMonthOfYear.merge(monthOfYear, amount, BigDecimal::add);
        }

        return paymentsByMonthOfYear;
    }

    private Map<String, BigDecimal> initializeWithZeroMonthsOfYear() {
        Map<String, BigDecimal> monthsOfYearMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM");

        YearMonth currentMonth = YearMonth.now();
        for (int month = 1; month <= 12; month++) {
            String monthOfYear = currentMonth.withMonth(month).format(formatter);
            monthsOfYearMap.put(monthOfYear, BigDecimal.ZERO);
        }

        return monthsOfYearMap;
    }
}
