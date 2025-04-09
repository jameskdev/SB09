package org.xm.sb09.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Content {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    @Column
    private Long id;
    @Getter
    @Setter
    @Column
    private String subject;
    @Getter
    @Setter
    @Column
    @Lob
    private String content;
    @Getter
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime submitTime;
    @Getter
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime lastModifiedDate;
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<ContentAttachment> uploadedFiles = new ArrayList<>();
    @ManyToOne
    private Account uploadedBy;

    protected Content() {
        this.subject = "";
        this.content = "";
    }

    public Account getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(Account uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Builder
    public Content(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void addFile(ContentAttachment file) {
        this.uploadedFiles.add(file);
    }

    public void deleteFile(ContentAttachment file) {
        this.uploadedFiles.remove(file);
    }

    public List<ContentAttachment> getUploadedFiles() {
        return Collections.unmodifiableList(uploadedFiles);
    }


}
