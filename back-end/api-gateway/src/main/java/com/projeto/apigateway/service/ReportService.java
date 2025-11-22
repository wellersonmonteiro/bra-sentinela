package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.ReportResponde;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {


    public ReportResponde generateReport() {
        return createDummyReportMock();

    }
    private ReportResponde createDummyReportMock() {
        int complaintsByPeriod = 150;
        List<String> typesOfScam = List.of("Phishing", "Identity Theft", "Credit Card Fraud");
        List<String> mustUsedChanne = List.of("Email", "Phone", "Social Media");

        return new ReportResponde(complaintsByPeriod, typesOfScam, mustUsedChanne);
    }
}
