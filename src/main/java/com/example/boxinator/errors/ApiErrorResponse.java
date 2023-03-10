package com.example.boxinator.errors;


public record ApiErrorResponse(
        String message,
        Integer statusCode,
        String path,
        String method,
        String timestamp
) {
}
