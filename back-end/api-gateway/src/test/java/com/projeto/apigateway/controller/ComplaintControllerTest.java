package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintCreateRequest;
import com.projeto.apigateway.controller.dto.ComplaintCreateResponse;
import com.projeto.apigateway.controller.dto.ComplaintListResponse;
import com.projeto.apigateway.controller.dto.ComplaintResponse;
import com.projeto.apigateway.controller.dto.ComplaintUpdateRequest;
import com.projeto.apigateway.controller.dto.ComplaintUpdateResponse;
import com.projeto.apigateway.service.ComplaintService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ComplaintControllerTest {
    @Mock
    private ComplaintService complaintService;
    @InjectMocks
    private ComplaintController complaintController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateComplaint() {
        ComplaintCreateRequest request = mock(ComplaintCreateRequest.class);
        ComplaintCreateResponse responseObj = mock(ComplaintCreateResponse.class);
        when(complaintService.createComplaint(request)).thenReturn(responseObj);
        ResponseEntity<ComplaintCreateResponse> response = complaintController.createComplaint(request);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }

    @Test
    void testGetComplaint() {
        ComplaintResponse responseObj = mock(ComplaintResponse.class);
        when(complaintService.getComplaintById("id")).thenReturn(responseObj);
        ResponseEntity<ComplaintResponse> response = complaintController.getComplaint("id");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }

    @Test
    void testUpdateComplaint() {
        ComplaintUpdateRequest request = mock(ComplaintUpdateRequest.class);
        ComplaintUpdateResponse responseObj = mock(ComplaintUpdateResponse.class);
        when(complaintService.updateComplaint(eq("id"), any(ComplaintUpdateRequest.class))).thenReturn(responseObj);
        ResponseEntity<ComplaintUpdateResponse> response = complaintController.updateComplaint("id", request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }

    @Test
    void testGetAllComplaints() {
        List<ComplaintListResponse> list = Arrays.asList(mock(ComplaintListResponse.class), mock(ComplaintListResponse.class));
        when(complaintService.getAllComplaints()).thenReturn(list);
        ResponseEntity<List<ComplaintListResponse>> response = complaintController.getAllComplaints();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(list, response.getBody());
    }

    @Test
    void testGetComplaintDetailsByCustomerId() {
        UUID uuid = UUID.randomUUID();
        var detailResponse = mock(com.projeto.apigateway.controller.dto.ComplaintDetailResponse.class);
        when(complaintService.getComplaintDetailsByCustomerId(uuid)).thenReturn(detailResponse);
        var response = complaintController.getComplaintDetailsByCustomerId(uuid);
        assertEquals(detailResponse, response);
    }
}
