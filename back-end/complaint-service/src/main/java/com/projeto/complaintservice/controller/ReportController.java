package com.projeto.complaintservice.controller;

import com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.complaintservice.service.ReportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/report")
@Slf4j
@AllArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateComplaintReport() {
        log.info("Generating report...");
        ComplaintReportQuantitiesResponse response = reportService.generateQuantitiesComplaintReport();

        return ResponseEntity.ok(response);
    }
}
