package org.xm.sb09.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    @Getter
    private String content;
    @ManyToOne(optional = false)
    @JoinColumn
    @Getter
    private Content postContent;
    @ManyToOne(optional = true)
    @JoinColumn
    @Getter
    private Account uploader;
    private String displayName;
    @Getter
    private String editPassword;
    @Getter
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime createdTime;
    @Getter
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private LocalDateTime lastModifiedDate;

    protected Comment() {
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void changeEditPassword(String editPassword) {
        this.editPassword = editPassword;
    }

    public String getDisplayName() {
        if (uploader == null) {
            return this.displayName;
        }
        return this.uploader.getDisplayName();
    }

}
