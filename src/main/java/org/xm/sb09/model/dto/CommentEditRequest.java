package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CommentEditRequest {
    private final Long commentId;
    private final String content;
    private final String password;

    public CommentEditRequest(@JsonProperty("comment_id") Long commentId, @JsonProperty("content") String content, 
        @JsonProperty("password") String password) {
        this.commentId = commentId;
        this.content = content;
        if (password == null) {
            this.password = "";
        } else {
            this.password = password;
        }
    }
}
