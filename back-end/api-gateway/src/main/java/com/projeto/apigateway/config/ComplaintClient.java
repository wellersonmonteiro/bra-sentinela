package com.projeto.apigateway.config;

import com.projeto.apigateway.controller.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}