package com.projeto.complaintservice.service;


import com.projeto.complaintservice.config.ComplaintProducer;
import com.projeto.complaintservice.controller.dto.ComplaintCreateRequest;
import com.projeto.complaintservice.controller.dto.ComplaintCreateResponse;
import com.projeto.complaintservice.controller.dto.ComplaintResponse;
import com.projeto.complaintservice.entity.ComplaintEntity;
import com.projeto.complaintservice.entity.LocationCity;
import com.projeto.complaintservice.exception.ComplaintException;
import com.projeto.complaintservice.repository.ComplaintRepository;
import com.projeto.complaintservice.repository.LocationCityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
@Slf4j
public class ComplaintService {
    public static final String PROT = "PROT-";
    private final ComplaintRepository complaintRepository;
    private final LocationCityRepository locationCityRepository;
    private final ComplaintProducer complaintProducer;

    public ComplaintCreateResponse createComplaint(ComplaintCreateRequest complaintRequest) {
        try {
            return saveComplaint(complaintRequest);
        } catch (ComplaintException complaintException) {
            log.error("Error creating complaint: {}", complaintException.getMessage());
            throw complaintException;
        }
    }

    public ComplaintResponse getComplaintById(String id) {
        try {

            return getCompaint(id);
        } catch (ComplaintException complaintException) {
            log.error("Error fetching complaint: {}", complaintException.getMessage());
            throw complaintException;
        }

    }

    private ComplaintCreateResponse saveComplaint(ComplaintCreateRequest complaintRequest) {
        try {
            String protocolNumber = getString();


            LocationCity locationCity = null;
            if (complaintRequest.getLocationCity() != null && complaintRequest.getLocationCity().getId() != null) {
                locationCity = locationCityRepository.findById(complaintRequest.getLocationCity().getId())
                        .orElse(locationCityRepository.save(complaintRequest.getLocationCity()));
            }

            ComplaintEntity complaintEntity = ComplaintEntity.builder()
                    .customerId(complaintRequest.getCustomerId())
                    .descriptionComplaint(complaintRequest.getDescription())
                    .description(complaintRequest.getDate())
                    .message(complaintRequest.getTime())
                    .channel(complaintRequest.getChannel())
                    .attackerName(complaintRequest.getAttackerName())
                    .value(complaintRequest.getValue())
                    .locationCity(locationCity)
                    .statusComplaint("Aberta")
                    .createdDate(LocalDateTime.now().toString())
                    .protocolNumber(protocolNumber)
                    .build();


            ComplaintEntity savedEntity = complaintRepository.save(complaintEntity);

            log.info("Complaint created with protocol: {}", savedEntity.getProtocolNumber());

            ComplaintCreateResponse response = ComplaintCreateResponse.builder()
                    .protocol(savedEntity.getProtocolNumber())
                    .build();

            complaintProducer.sendComplaintCreated(response);
            return response;

        } catch (Exception e) {
            log.error("Error saving complaint: {}", e.getMessage());
            throw new ComplaintException("Failed to create complaint", "NotCreatedError");
        }
    }

    private static String getString() {
        return PROT + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private ComplaintResponse getCompaint(String id) {
        ComplaintEntity complaintEntity = complaintRepository.findByProtocolNumber(id);

        if (complaintEntity == null) {
            throw new ComplaintException("Complaint not found", "NotFoundError");
        }
        return ComplaintResponse.builder()
                .statusComplaint(complaintEntity.getStatusComplaint())
                .createdDate(complaintEntity.getCreatedDate())
                .descriptionComplaint(complaintEntity.getDescriptionComplaint())
                .protocolNumber(complaintEntity.getProtocolNumber())
                .message("Sua solicitação está aguardando análise!")
                .build();
    }
}


