package org.xm.sb09.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
public class ContentAttachment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    private Long id;
    @ManyToOne
    private Content content;
    @Getter
    @Lob
    private byte[] attachmentFile;

    protected ContentAttachment() {
    }

    @Builder
    public ContentAttachment(Content content, byte[] uploadedFile) {
        this.content = content;
        this.attachmentFile = uploadedFile;
    }
}
