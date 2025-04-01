package org.xm.sb09.model.dto;

import org.springframework.http.HttpStatus;

public class ContentSubmitResponse {
    private final String submitTime;
    private final String id;
    private final HttpStatus responseCode;

    public ContentSubmitResponse(String id, String submitTime, HttpStatus responseCode) {
        this.id = id;
        this.submitTime = submitTime;
        this.responseCode = responseCode;
    }

    public String getId() {
        return this.id;
    }

    public String getSubmitTime() {
        return this.submitTime;
    }

    public HttpStatus getResponseCode() {
        return this.responseCode;
    }
}
