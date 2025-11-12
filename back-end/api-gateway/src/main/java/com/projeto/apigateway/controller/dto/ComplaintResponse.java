package com.projeto.apigateway.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ComplaintResponse {
    private String protocol;
}
