package org.xm.sb09.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentEditRequest {
    private final Long commentId;
    private final String content;
    private final String password;
}
