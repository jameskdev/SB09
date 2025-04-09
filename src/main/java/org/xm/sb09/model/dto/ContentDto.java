package org.xm.sb09.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ContentDto {
    @Getter
    private final Long id;
    @Getter
    private final String subject;
    @Getter
    private final String content;
    @Getter
    private final AccountDto uploadedBy;
    @Getter
    private final LocalDateTime uploadedDate;
    @Getter
    private final LocalDateTime lastModifiedDate;
    private final List<ContentAttachmentDto> attachments = new ArrayList<>();

    public List<ContentAttachmentDto> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    public void addAttachment(ContentAttachmentDto attachment) {
        attachments.add(attachment);
    }
}
