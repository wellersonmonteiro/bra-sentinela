package com.projeto.apigateway.service;

import com.projeto.apigateway.config.ComplaintClient;
import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReportService {
    private final ComplaintClient complaintClient;
    public ComplaintReportQuantitiesResponse generatedCountReport() {
        return complaintClient.getComplaintReportQuantities();
    }
}
