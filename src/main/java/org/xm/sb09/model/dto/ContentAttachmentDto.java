package org.xm.sb09.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContentAttachmentDto {
    private final Long id;
    private final byte[] attachmentFile;
}
