package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final com.projeto.apigateway.config.ComplaintClient complaintServiceClient;

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateReportQuantities() {

        return ResponseEntity.ok(reportService.generatedCountReport());
    }

    @GetMapping("/v1/report/last-months/pdf")
    public ResponseEntity<byte[]> proxyLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months) {
        byte[] pdfBytes = complaintServiceClient.getLastMonthsPdf(months);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "relatorio-denuncias.pdf");
        headers.setContentLength(pdfBytes != null ? pdfBytes.length : 0);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
