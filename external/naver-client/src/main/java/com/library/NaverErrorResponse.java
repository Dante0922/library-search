package com.library;

import lombok.Getter;

@Getter
public class NaverErrorResponse {
    private String errorMessage;
    private String errorCode;

    public NaverErrorResponse(String errorMessage, String errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
