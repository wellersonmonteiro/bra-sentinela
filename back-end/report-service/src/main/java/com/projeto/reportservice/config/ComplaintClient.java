package com.projeto.reportservice.config;

import com.projeto.reportservice.controller.dto.ComplaintListResponse;
import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "complaint-service", url = "http://localhost:3002")
public interface ComplaintClient {

    @GetMapping("/v1/complaint")
    List<ComplaintListResponse> getComplaints();

    @GetMapping("/v1/report")
    ComplaintReportQuantitiesResponse getComplaintReportQuantities();

    @GetMapping("/v1/report/last-months")
    List<MonthlyComplaintDetailedReportResponse> getLastMonthsDetailed(@RequestParam(name = "months", defaultValue = "6") int months);

    @GetMapping(value = "/v1/report/last-months/pdf", produces = "application/pdf")
    byte[] getLastMonthsPdf(@RequestParam(name = "months", defaultValue = "6") int months);
}
