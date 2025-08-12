package com.library.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatResponse {
    private String query;
    private long count;

    public StatResponse(String query, long count) {
        this.query = query;
        this.count = count;
    }
}
