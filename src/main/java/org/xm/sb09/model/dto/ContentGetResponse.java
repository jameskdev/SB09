package org.xm.sb09.model.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.xm.sb09.model.Content;

import lombok.Getter;

@Getter
public class ContentGetResponse {
    private final List<ContentDto> results;
    private final HttpStatus responseCode;
    private int resultSize;
    private final String message;

    public ContentGetResponse(List<Content> results, HttpStatus responseCode, String message) {
        this.results = new ArrayList<>();
        results.forEach(x -> {
            ContentDto c = new ContentDto(x.getId(), x.getSubject(), x.getContent(), new AccountDto(x.getUploadedBy().getId(), x.getUploadedBy().getDisplayName(), x.getUploadedBy().getIdentifier()));
            x.getUploadedFiles().forEach(a -> c.addAttachment(new ContentAttachmentDto(a.getId(), a.getAttachmentFile())));
            this.results.add(c);
        });
        this.responseCode = responseCode;
        this.message = message;
        this.resultSize = results.size();
    }
}
