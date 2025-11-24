package com.projeto.apigateway.controller.dto;

public record UserCreateRequest(String fullName, String email, String phone, String cpf) {
}

