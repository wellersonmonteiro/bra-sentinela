package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.ComplaintCreateRequest;
import com.projeto.apigateway.controller.dto.ComplaintCreateResponse;
import com.projeto.apigateway.controller.dto.ComplaintResponse;
import com.projeto.apigateway.exceptions.ComplaintException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class ComplaintService {
    public static final String PROTOCOL = "ABC01112025";

    public ComplaintCreateResponse createComplaint(ComplaintCreateRequest complaintRequest) {
        try {
            saveMock(complaintRequest);
            return ComplaintCreateResponse.builder().protocol(PROTOCOL).build();
        } catch (ComplaintException complaintException) {
            log.error("Error creating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }
    }

    public ComplaintResponse getComplaintById(String id) {
        try {

            return getMock(id);
        } catch (ComplaintException complaintException) {
            log.error("Error fetching complaint: {}", complaintException.getMessage());
            throw complaintException;
        }

    }

    private void saveMock(ComplaintCreateRequest complaintRequest) {
        if (complaintRequest.getCustomerId() != null) {
            log.info("Complaint created with success!");
        } else {
            throw new ComplaintException("Failed to create complaint", "NotCreatedError");
        }

    }

    private ComplaintResponse getMock(String id) {
        if (id.equals(PROTOCOL)) {
            throw new ComplaintException("Complaint not found", "NotFoundError");
        } else {
            ComplaintResponse complaintResponse = ComplaintResponse.builder().statusComplaint("IN_PROGRESS")
                    .createdDate("01-11-2025")
                    .descriptionComplaint("Complaint description example")
                    .protocolNumber(PROTOCOL)
                    .message("Complaint found with success!")
                    .build();
            log.info("Complaint found with success!");
            return complaintResponse;

        }
    }

}
