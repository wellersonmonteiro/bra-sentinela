package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.controller.dto.ComplaintUpdateRequest;
import com.projeto.apigateway.controller.dto.ComplaintUpdateResponse;
import com.projeto.apigateway.service.AnalysisService;
import com.projeto.apigateway.service.ComplaintService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisControllerTest {

    private AnalysisService analysisService;
    private ComplaintService complaintService;
    private AnalysisController controller;

    @BeforeEach
    void setUp() {
        analysisService = mock(AnalysisService.class);
        complaintService = mock(ComplaintService.class);
        controller = new AnalysisController(analysisService, complaintService);
    }

    @Test
    void analyzeComplaints_returnsResponse() {
        var req = new AnalysisRequest("validada", "interno", "publico", List.of());
        var resp = new AnalysisResponse("ok");
        when(analysisService.analyzeComplaints(req)).thenReturn(resp);

        var result = controller.analyzeComplaints(req);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }

    @Test
    void updateComplaintAnalysis_returns203() {
        var req = new AnalysisRequest("validada", "interno", "publico", List.of());
        var updateReq = new ComplaintUpdateRequest("validada", "interno", "publico", List.of());
        var updateResp = new ComplaintUpdateResponse("Complaint updated successfully");
        when(complaintService.updateComplaint("PROTO-1234", updateReq)).thenReturn(updateResp);

        var result = controller.updateComplaintAnalysis("PROTO-1234", req);
        assertEquals(203, result.getStatusCodeValue());
        assertEquals(updateResp, result.getBody());
    }
}
