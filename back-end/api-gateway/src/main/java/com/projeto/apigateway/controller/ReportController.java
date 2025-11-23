package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ComplaintReportQuantitiesResponse> generateReportQuantities() {

        return ResponseEntity.ok(reportService.generatedCountReport());
    }
}
