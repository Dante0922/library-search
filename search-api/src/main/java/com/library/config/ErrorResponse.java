package com.library.config;

import library.exception.ErrorType;

public record ErrorResponse(String errorMessage, ErrorType errorType) {
}
