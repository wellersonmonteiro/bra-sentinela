package com.projeto.complaintservice.controller.dto;

import com.projeto.complaintservice.entity.LocationCity;
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
        LocationCity locationCity,
        List<String> files,
        String date,
        String time
) {
}