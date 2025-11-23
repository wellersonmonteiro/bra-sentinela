package com.projeto.complaintservice.controller.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintListResponse {
    
    private UUID id;
    private String protocol;
    private String date;
    private String channel;
    private String status;
}