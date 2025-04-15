package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CommentPostRequest {
    private final Long postId;
    private final String content;

    public CommentPostRequest(@JsonProperty("post_id") Long postId, @JsonProperty("content") String content) {
        this.postId = postId;
        this.content = content;
    }
}
