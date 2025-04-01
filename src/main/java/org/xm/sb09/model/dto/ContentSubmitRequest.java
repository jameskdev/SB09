package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentSubmitRequest {
    private final String subject;
    private final String content;

    public ContentSubmitRequest(@JsonProperty("subject") String subject, @JsonProperty("content") String content) {
        this.subject = subject;
        this.content = content;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }
}
