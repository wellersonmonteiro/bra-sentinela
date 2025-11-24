package com.projeto.complaintservice.controller;

import com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.complaintservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.complaintservice.service.PdfReportService;
import com.projeto.complaintservice.service.ReportService;
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
        log.info("Generating report...");
        ComplaintReportQuantitiesResponse response = reportService.generateQuantitiesComplaintReport();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/last-months")
    public ResponseEntity<List<MonthlyComplaintDetailedReportResponse>> generateLastNMonthsDetailedReport(
            @RequestParam(name = "months", defaultValue = "6") int months) {
        log.info("Generating last {} months detailed report...", months);
        List<MonthlyComplaintDetailedReportResponse> response = reportService.generateLastNMonthsDetailedReport(months);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/last-months/pdf")
    public ResponseEntity<byte[]> generateLastNMonthsDetailedReportPdf(
            @RequestParam(name = "months", defaultValue = "6") int months) {
        log.info("Generating PDF for last {} months detailed report...", months);

        byte[] pdfBytes = pdfReportService.generateMonthlyReportPdf(months);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-denuncias.pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
    @GetMapping("/complaints/csv/all")
    public ResponseEntity<byte[]> exportAllComplaintsCsv() {
        byte[] csvBytes = reportService.exportComplaintsCsv(null, null);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "complaints.csv");
        headers.setContentLength(csvBytes.length);
        return ResponseEntity.ok()
            .headers(headers)
            .body(csvBytes);
    }
}
