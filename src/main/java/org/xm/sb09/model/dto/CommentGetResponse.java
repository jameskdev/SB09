package org.xm.sb09.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.xm.sb09.model.Comment;

import lombok.Getter;

public class CommentGetResponse {
    private final List<CommentDto> commentList = new ArrayList<>();
    @Getter
    private final String message;
    @Getter
    private final HttpStatus responseCode;

    public CommentGetResponse(List<Comment> commentList, String message, HttpStatus responseCode) {
        commentList.stream().map(CommentDto::fromEntity).forEach(this.commentList::add);
        this.message = message;
        this.responseCode = responseCode;
    }

    public List<CommentDto> getCommentList() {
        return Collections.unmodifiableList(commentList);
    }
}