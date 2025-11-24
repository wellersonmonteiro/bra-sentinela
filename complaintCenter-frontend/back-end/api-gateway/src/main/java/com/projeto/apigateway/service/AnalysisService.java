package com.projeto.apigateway.service;

import com.projeto.apigateway.controller.dto.AnalysisRequest;
import com.projeto.apigateway.controller.dto.AnalysisResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class AnalysisService {

    public AnalysisResponse analyzeComplaints(AnalysisRequest analysisRequest) {

        String analysisResponse = "Análise concluída: As reclamações foram analisadas com sucesso.";
        return new AnalysisResponse(analysisResponse);
    }


}
