package com.projeto.complaintservice.controller.dto;

import com.projeto.complaintservice.entity.LocationCity;
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
        LocationCity locationCity,
        List<String> files,
        String value,
        String date,
        String time
) {
}