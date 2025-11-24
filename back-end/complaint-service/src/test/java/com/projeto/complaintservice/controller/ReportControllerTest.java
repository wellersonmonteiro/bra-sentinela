package com.projeto.complaintservice.controller;

import com.projeto.complaintservice.service.ReportService;
import com.projeto.complaintservice.service.PdfReportService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @Mock
    private ReportService reportService;
    @Mock
    private PdfReportService pdfReportService;
    @InjectMocks
    private ReportController reportController;

    @Test
    void testExportAllComplaintsCsv() {
        byte[] csvBytes = "id,protocolo\n1,PROT-123".getBytes();
        when(reportService.exportComplaintsCsv(null, null)).thenReturn(csvBytes);
        ResponseEntity<byte[]> response = reportController.exportAllComplaintsCsv();
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(csvBytes, response.getBody());
        assertTrue(response.getHeaders().getContentType().toString().contains("csv"));
    }

    @Test
    void testGenerateComplaintReport() {
        when(reportService.generateQuantitiesComplaintReport()).thenReturn(mock(com.projeto.complaintservice.controller.dto.ComplaintReportQuantitiesResponse.class));
        ResponseEntity<?> response = reportController.generateComplaintReport();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGenerateLastNMonthsDetailedReport() {
        when(reportService.generateLastNMonthsDetailedReport(6)).thenReturn(java.util.Collections.emptyList());
        ResponseEntity<?> response = reportController.generateLastNMonthsDetailedReport(6);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGenerateLastNMonthsDetailedReportPdf() {
        byte[] pdfBytes = "PDFDATA".getBytes();
        when(pdfReportService.generateMonthlyReportPdf(6)).thenReturn(pdfBytes);
        ResponseEntity<byte[]> response = reportController.generateLastNMonthsDetailedReportPdf(6);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(pdfBytes, response.getBody());
        assertTrue(response.getHeaders().getContentType().toString().contains("pdf"));
    }

    // Adicione outros testes para os endpoints restantes conforme necessário
}
