package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CommentAnonymousPostRequest {
    private final Long postId;
    private final String content;
    private final String displayName;
    private final String password;

    public CommentAnonymousPostRequest(@JsonProperty("post_id") Long postId, @JsonProperty("content") String content, 
            @JsonProperty("display_name") String displayName, @JsonProperty("password") String password) {
        this.postId = postId;
        this.content = content;
        this.displayName = displayName;
        this.password = password;
    }
}