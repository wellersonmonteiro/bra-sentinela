package com.projeto.reportservice.service;

import com.projeto.reportservice.config.ComplaintClient;
import com.projeto.reportservice.controller.dto.ComplaintListResponse;
import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportService {

    private final ComplaintClient complaintClient;

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

        LocalDate s = LocalDate.parse(start);
        LocalDate e = LocalDate.parse(end);

        List<ComplaintListResponse> all = Optional.ofNullable(complaintClient.getComplaints()).orElse(Collections.emptyList());

        List<ComplaintListResponse> filtered = all.stream()
            .filter(c -> {
                LocalDate d = tryParseDate(c.getDate());
                if (d == null) {
                    log.warn("Skipping complaint with invalid date: id={}, date={}", c.getId(), c.getDate());
                    return false;
                }
                return !d.isBefore(s) && !d.isAfter(e);
            })
            .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("id,createdDate,channel,status\n");

        for (ComplaintListResponse c : filtered) {
            String id = c.getId() != null ? c.getId().toString() : "";
            String created = c.getDate() != null ? ((String) c.getDate()).replace("\n", " ").replace("\r", " ") : "";
            String channel = c.getChannel() != null ? ((String) c.getChannel()).replace("\"", "\"\"").replace("\n", " ") : "";
            String status = c.getStatus() != null ? c.getStatus().replace("\n", " ") : "";

            sb.append('"').append(id).append('"').append(',');
            sb.append('"').append(created).append('"').append(',');
            sb.append('"').append(channel).append('"').append(',');
            sb.append('"').append(status).append('"').append('\n');
        }

        log.info("Generated CSV: totalRecords={}, filtered={}", all.size(), filtered.size());
        return sb.toString().getBytes(StandardCharsets.UTF_8);
}

    private LocalDate tryParseDate(Object date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryParseDate'");
    }
}
