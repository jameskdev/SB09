package org.xm.sb09.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    @Getter
    private String content;
    @ManyToOne(optional = false)
    @Getter
    private Content postContent;
    @ManyToOne(optional = true)
    @Getter
    private Account uploader;
    private String displayName;
    @Getter
    private String editPassword;

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
