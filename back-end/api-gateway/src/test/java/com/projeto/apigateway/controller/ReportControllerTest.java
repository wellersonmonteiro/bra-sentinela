package com.projeto.apigateway.controller;

import com.projeto.apigateway.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.apigateway.service.ReportService;
import com.projeto.apigateway.config.ComplaintClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerTest {
    @Mock
    private ReportService reportService;
    @Mock
    private ComplaintClient complaintServiceClient;
    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReportQuantities() {
        ComplaintReportQuantitiesResponse responseObj = mock(ComplaintReportQuantitiesResponse.class);
        when(reportService.generatedCountReport()).thenReturn(responseObj);
        ResponseEntity<ComplaintReportQuantitiesResponse> response = reportController.generateReportQuantities();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseObj, response.getBody());
    }

    @Test
    void testProxyLastMonthsPdf() {
        byte[] pdfBytes = new byte[]{1,2,3};
        when(complaintServiceClient.getLastMonthsPdf(6)).thenReturn(pdfBytes);
        ResponseEntity<byte[]> response = reportController.proxyLastMonthsPdf(6);
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals("attachment; filename=relatorio-denuncias.pdf", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        assertArrayEquals(pdfBytes, response.getBody());
    }

    @Test
    void testProxyAllComplaintsCsv() {
        byte[] csvBytes = new byte[]{1,2,3};
        ResponseEntity<byte[]> mockResponse = ResponseEntity.ok().body(csvBytes);
        when(complaintServiceClient.exportAllComplaintsCsv()).thenReturn(mockResponse);
        ResponseEntity<byte[]> response = reportController.proxyAllComplaintsCsv();
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(csvBytes, response.getBody());
    }
}
