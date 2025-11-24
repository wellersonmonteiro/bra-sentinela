package com.projeto.reportservice.service;

import com.projeto.reportservice.config.ComplaintClient;
import com.projeto.reportservice.controller.dto.ComplaintListResponse;
import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final ComplaintClient complaintClient;

    public ReportService(ComplaintClient complaintClient) {
        this.complaintClient = complaintClient;
    }

    public ComplaintReportQuantitiesResponse getReportQuantities() {
        return complaintClient.getComplaintReportQuantities();
    }

    public List<MonthlyComplaintDetailedReportResponse> getLastMonthsDetailed(int months) {
        return complaintClient.getLastMonthsDetailed(months);
    }

    public byte[] generateComplaintsCsv(String start, String end) {
        if (start == null || start.isBlank()) {
            start = LocalDate.now().minusMonths(5).withDayOfMonth(1).toString();
        }
        if (end == null || end.isBlank()) {
            end = LocalDate.now().toString();
        }

        // parse requested date range (ISO yyyy-MM-dd expected)
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        // fetch complaints via Feign client and defensively handle nulls
        List<ComplaintListResponse> all = Optional.ofNullable(complaintClient.getComplaints())
                .orElse(Collections.emptyList());

        // filter by parsed date range; invalid dates are skipped and logged
        List<ComplaintListResponse> filtered = all.stream()
                .filter(c -> {
                    LocalDate d = tryParseDate(c.getDate());
                    if (d == null) {
                        log.warn("Skipping complaint with invalid date: id={}, date={}", c.getId(), c.getDate());
                        return false;
                    }
                    return !d.isBefore(startDate) && !d.isAfter(endDate);
                })
                .collect(Collectors.toList());

        // build CSV using a StringBuilder (ok for moderate sizes). For very large exports
        // prefer a streaming approach (StreamingResponseBody) to avoid high memory usage.
        StringBuilder sb = new StringBuilder();
        sb.append(csvHeader());

        for (ComplaintListResponse c : filtered) {
            sb.append(csvRow(c)).append('\n');
        }

        log.info("Generated CSV: totalRecords={}, filtered={}", all.size(), filtered.size());
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String csvHeader() {
        return "id,createdDate,channel,status";
    }

    private String csvRow(ComplaintListResponse c) {
        // Helper to escape and normalize CSV fields
        String id = safeString(c.getId() != null ? c.getId().toString() : "");
        String created = safeString(c.getDate());
        String channel = safeString(c.getChannel());
        String status = safeString(c.getStatus());

        return '"' + id + '"' + ',' + '"' + created + '"' + ',' + '"' + channel + '"' + ',' + '"' + status + '"';
    }

    private String safeString(String input) {
        if (input == null) return "";
        // replace newlines and escape double quotes for CSV
        return input.replace('\n', ' ').replace('\r', ' ').replace("\"", "\"\"");
    }
    private LocalDate tryParseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) return null;

        // Normaliza: se vier com hora, pega os 10 primeiros caracteres (yyyy-MM-dd)
        String candidate = dateStr.length() > 10 ? dateStr.substring(0, 10) : dateStr;

        DateTimeFormatter iso = DateTimeFormatter.ISO_LOCAL_DATE;
        try {
            return LocalDate.parse(candidate, iso);
        } catch (DateTimeParseException ex) {
            // Tentar outros formatos se necessário (ex.: dd/MM/yyyy)
            try {
                DateTimeFormatter alt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(candidate, alt);
            } catch (DateTimeParseException ex2) {
                log.debug("tryParseDate: não foi possível parsear '{}'", dateStr);
                return null;
            }
        }
    }
}
