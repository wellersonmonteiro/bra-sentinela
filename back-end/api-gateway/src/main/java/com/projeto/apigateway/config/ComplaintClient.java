package com.projeto.apigateway.config;

import com.projeto.apigateway.controller.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(
    name = "complaint-service",
    url = "http://localhost:3002"
)
public interface ComplaintClient {
    
    @GetMapping("/v1/complaints")
    List<ComplaintResponse> getAllComplaints();
    
    @GetMapping("/v1/complaint/{id}")
    ComplaintResponse getComplaintById(@PathVariable String id);
    
    @PostMapping("/v1/complaint")
    ComplaintCreateResponse createComplaint(@RequestBody ComplaintCreateRequest request);

    @PutMapping("/v1/complaint/{id}")
    ComplaintUpdateResponse updateComplaint(@PathVariable String id, @RequestBody ComplaintUpdateRequest request);

    @GetMapping("/v1/complaint")
    List<ComplaintListResponse> getComplaints();

    @GetMapping("/v1/complaint/customer/{id}/details")
    ComplaintDetailResponse getComplaintDetailsByCustomerId(@PathVariable UUID id);

    @GetMapping("v1/report")
    ComplaintReportQuantitiesResponse getComplaintReportQuantities();

    @GetMapping(value = "/v1/report/last-months/pdf", produces = "application/pdf")
    byte[] getLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months);

    @GetMapping("/v1/report/complaints/csv/all")
    ResponseEntity<byte[]> exportAllComplaintsCsv();
}