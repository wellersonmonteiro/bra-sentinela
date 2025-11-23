package com.projeto.complaintservice.service;

import com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.complaintservice.repository.ComplaintRepository;
import com.projeto.complaintservice.repository.LocationCityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
