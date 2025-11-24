package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.service.AnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisControllerTest {

    private AnalysisService analysisService;
    private AnalysisController controller;

    @BeforeEach
    void setUp() {
        analysisService = mock(AnalysisService.class);
        controller = new AnalysisController(analysisService);
    }

    @Test
    void analyzeComplaints_returnsResponse() {
        var req = new AnalysisRequest(List.of("p1"));
        var resp = new AnalysisResponse("ok");
        when(analysisService.analyzeComplaints(req)).thenReturn(resp);

        var result = controller.analyzeComplaints(req);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }
}
