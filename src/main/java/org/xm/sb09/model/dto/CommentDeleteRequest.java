package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CommentDeleteRequest {
    private final Long commentId;
    private final String password;

    public CommentDeleteRequest(@JsonProperty("comment_id") Long commentId, @JsonProperty("password") String password) {
        this.commentId = commentId;
        if (password == null) {
            this.password = "";
        } else {
            this.password = password;
        }
    }
}
