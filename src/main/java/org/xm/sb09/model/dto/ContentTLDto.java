package org.xm.sb09.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

public class ContentTLDto {
    @Getter
    private final Long id;
    @Getter
    private final String subject;
    @Getter
    private final String content;
    @Getter
    private final String uploader;
    @Getter
    private final LocalDateTime uploadedDate;
    @Getter
    private final LocalDateTime lastModifiedDate;
    private final List<Long> attachmentList;

    @Builder
    public ContentTLDto(Long id, String subject, String content, String uploader, LocalDateTime uploadedDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.uploader = uploader;
        this.uploadedDate = uploadedDate;
        this.lastModifiedDate = lastModifiedDate;
        this.attachmentList = new ArrayList<>();
    }

    public List<Long> getAttachmentList() {
        return Collections.unmodifiableList(this.attachmentList);
    }

    public void addAttachmentId(Long id) {
        this.attachmentList.add(id);
    }
}
