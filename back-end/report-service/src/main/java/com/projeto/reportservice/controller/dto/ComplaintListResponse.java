package com.projeto.reportservice.controller.dto;

import lombok.*;
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
