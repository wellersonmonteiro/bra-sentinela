package com.projeto.reportservice.controller;

import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.reportservice.service.PdfReportService;
import com.projeto.reportservice.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/report")
@Slf4j
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final PdfReportService pdfReportService;

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateComplaintReport() {
        log.info("Proxying report quantities from complaint-service via report-service");
        return ResponseEntity.ok(reportService.getReportQuantities());
    }

    @GetMapping("/last-months")
    public ResponseEntity<List<MonthlyComplaintDetailedReportResponse>> generateLastNMonthsDetailedReport(
            @RequestParam(name = "months", defaultValue = "6") int months) {
        log.info("Proxying last {} months detailed report", months);
        return ResponseEntity.ok(reportService.getLastMonthsDetailed(months));
    }

    @GetMapping("/last-months/pdf")
    public ResponseEntity<byte[]> generateLastNMonthsDetailedReportPdf(
            @RequestParam(name = "months", defaultValue = "6") int months) {
        log.info("Generating PDF in report-service for last {} months detailed report...", months);

        byte[] pdfBytes = pdfReportService.generateMonthlyReportPdf(months);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-denuncias.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/complaints/csv")
    public ResponseEntity<byte[]> downloadComplaintsCsv(
            @RequestParam(name = "start", required = false) String start,
            @RequestParam(name = "end", required = false) String end) {
        log.info("Generating complaints CSV (report-service) from {} to {}", start, end);

        byte[] csvBytes = reportService.generateComplaintsCsv(start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        String fileName = String.format("denuncias_%s_to_%s.csv", (start != null ? start : "inicio"), (end != null ? end : "fim"));
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(csvBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvBytes);
    }
}
