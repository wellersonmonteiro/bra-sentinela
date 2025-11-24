package com.projeto.apigateway.controller.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ComplaintResponse(
        String statusComplaint,
        String createdDate,
        String descriptionComplaint,
        String protocolNumber,
        String message,
        String channel,
        String attackerName,
        String value,
        Object locationCity, // Usei Object para aceitar o JSON sem precisar criar a classe LocationCity no Gateway
        List<String> files,
        String date,
        String time
) {
}