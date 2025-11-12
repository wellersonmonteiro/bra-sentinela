package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.ComplaintRequest;
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

    public ComplaintResponse createComplaint(ComplaintRequest complaintRequest){
        try {
            saveMock(complaintRequest);
            return ComplaintResponse.builder().protocol(PROTOCOL).build();
        }catch (ComplaintException complaintException){
            log.error("Error creating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }


    }
    private void saveMock(ComplaintRequest complaintRequest){
        if(complaintRequest.getCustomerId() != null){
            log.info("Complaint created with success!");
        } else{
            throw new ComplaintException("Failed to create complaint", "NotCreatedError");
        }

    }

}
