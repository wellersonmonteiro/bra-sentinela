package com.projeto.complaintservice.service;

import com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.complaintservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.complaintservice.repository.ComplaintRepository;
import com.projeto.complaintservice.repository.LocationCityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ReportService {
    private final ComplaintRepository complaintRepository;
    private final LocationCityRepository locationCityRepository;


    public ComplaintReportQuantitiesResponse generateQuantitiesComplaintReport() {
        Long totalOpenComplaints = complaintRepository.countByStatusComplaint("ABERTA");
        Long totalClosedComplaints = complaintRepository.countByStatusComplaint("EM ANÁLISE");
        Long totalInProgressComplaints = complaintRepository.countByStatusComplaint("VALIDADA");
        Long totalInvelidedComplaints = complaintRepository.countByStatusComplaint("INCONSISTENTE");


        return new ComplaintReportQuantitiesResponse(
                totalOpenComplaints,
                totalClosedComplaints,
                totalInProgressComplaints,
                totalInvelidedComplaints
        );
    }
    public List<MonthlyComplaintDetailedReportResponse> generateLastNMonthsDetailedReport(int months) {
        if (months <= 0) months = 6;

        LocalDate startDate = LocalDate.now().withDayOfMonth(1).minusMonths(months - 1);
        List<Object[]> rows = complaintRepository.countComplaintsSinceDetailed(startDate.toString());
        List<MonthlyComplaintDetailedReportResponse> result = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            Object[] row = rows.get(i);

            String yearMonth = row[0] != null ? row[0].toString() : null;
            Integer year = row[1] != null ? ((Number) row[1]).intValue() : null;
            Integer month = row[2] != null ? ((Number) row[2]).intValue() : null;
            String monthName = row[3] != null ? row[3].toString().trim() : null;
            Long total = row[4] != null ? ((Number) row[4]).longValue() : 0L;
            Long pending = row[5] != null ? ((Number) row[5]).longValue() : 0L;
            Long open = row[6] != null ? ((Number) row[6]).longValue() : 0L;
            Long inProgress = row[7] != null ? ((Number) row[7]).longValue() : 0L;
            Long validated = row[8] != null ? ((Number) row[8]).longValue() : 0L;
            Long inconsistent = row[9] != null ? ((Number) row[9]).longValue() : 0L;
            Double averagePerDay = row[10] != null ? ((Number) row[10]).doubleValue() : 0.0;


            Double percentageChange = 0.0;
            if (i > 0) {
                Object[] previousRow = rows.get(i - 1);
                Long previousTotal = previousRow[4] != null ? ((Number) previousRow[4]).longValue() : 0L;
                if (previousTotal > 0) {
                    percentageChange = ((total - previousTotal) * 100.0) / previousTotal;
                }
            }

            result.add(new MonthlyComplaintDetailedReportResponse(
                    yearMonth, year, month, monthName, total, pending, open,
                    inProgress, validated, inconsistent, averagePerDay,
                    Math.round(percentageChange * 100.0) / 100.0
            ));
        }

        return result;
    }
}
