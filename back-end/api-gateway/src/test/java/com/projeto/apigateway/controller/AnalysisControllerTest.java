package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import com.projeto.apigateway.service.AnalysisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalysisControllerTest {
    @Mock
    private AnalysisService analysisService;
    @InjectMocks
    private AnalysisController analysisController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAnalyzeComplaints() {
        AnalysisRequest request = mock(AnalysisRequest.class);
        AnalysisResponse responseObj = mock(AnalysisResponse.class);
        when(analysisService.analyzeComplaints(request)).thenReturn(responseObj);
        ResponseEntity<AnalysisResponse> response = analysisController.analyzeComplaints(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }
}
