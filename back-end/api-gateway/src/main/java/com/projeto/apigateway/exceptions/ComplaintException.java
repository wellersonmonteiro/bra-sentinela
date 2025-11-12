package com.projeto.apigateway.exceptions;

public class ComplaintException extends RuntimeException {
    private String error;
    public ComplaintException(String message, String error) {
        super(message);
        this.error = error;
    }


}
