package org.xm.sb09.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Getter;

@Entity
public class Content {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Id
    private Long id;
    @Getter
    private String subject;
    @Getter
    @Lob
    private String content;
    @Getter
    @Temporal(TemporalType.TIME)
    private Date submitTime;
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    private List<ContentAttachment> uploadedFiles = new ArrayList<>();

    protected Content() {
        this.subject = "";
        this.content = "";
    }

    @PrePersist
    public void setSubmitTime() {
        submitTime = new Date();
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
