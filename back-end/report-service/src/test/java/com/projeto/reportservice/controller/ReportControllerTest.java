package com.projeto.reportservice.controller;

import com.projeto.reportservice.controller.dto.ComplaintReportQuantitiesResponse;
import com.projeto.reportservice.controller.dto.MonthlyComplaintDetailedReportResponse;
import com.projeto.reportservice.service.PdfReportService;
import com.projeto.reportservice.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportControllerTest {

    private ReportService reportService;
    private PdfReportService pdfReportService;
    private ReportController controller;

    @BeforeEach
    void setUp() {
        reportService = mock(ReportService.class);
        pdfReportService = mock(PdfReportService.class);
        controller = new ReportController(reportService, pdfReportService);
    }

    @Test
    void generateComplaintReport_returnsOk() {
        var resp = new ComplaintReportQuantitiesResponse(1L,2L,3L,4L);
        when(reportService.getReportQuantities()).thenReturn(resp);

        var result = controller.generateComplaintReport();

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(resp, result.getBody());
    }

    @Test
    void generateLastMonthsPdf_returnsPdfBytes() {
        byte[] pdf = new byte[]{1,2,3};
        when(pdfReportService.generateMonthlyReportPdf(6)).thenReturn(pdf);

        var result = controller.generateLastNMonthsDetailedReportPdf(6);

        assertEquals(200, result.getStatusCodeValue());
        assertArrayEquals(pdf, result.getBody());
    }
}
