package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentSubmitRequest {
    private final String subject;
    private final String content;
    private final Long id;

    public ContentSubmitRequest(@JsonProperty("subject") String subject, @JsonProperty("content") String content, @JsonProperty("id") Long id) {
        this.subject = subject;
        this.content = content;
        if (id != null) {
            this.id = id;
        } else {
            this.id = 1L;
        }
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }

    public Long getId() {
        return this.id;
    }
}
