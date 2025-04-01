package org.xm.sb09.model.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.xm.sb09.model.Content;

import lombok.Getter;

@Getter
public class ContentGetResponse {
    private final List<Content> results;
    private final HttpStatus responseCode;
    private int resultSize;
    private final String message;

    public ContentGetResponse(List<Content> results, HttpStatus responseCode, String message) {
        this.results = results;
        this.responseCode = responseCode;
        this.message = message;
        this.resultSize = results.size();
    }
}
