package com.projeto.apigateway.controller.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record ComplaintDetailResponse(
        String protocol,
        String status,
        String description,
        String createdAt,
        String channel,
        String attackerName,
        Object locationCity, // Usei Object aqui também
        List<String> files,
        String value,
        String date,
        String time
) {
}