package com.projeto.complaintservice.controller.dto;

public record ComplaintReportQuantitiesResponse (Long open, Long pendingReview, Long validated, Long inconsistent ) {
}
