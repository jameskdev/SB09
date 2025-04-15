package org.xm.sb09.model.dto;

import org.xm.sb09.model.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CommentDto {
    private final boolean isAnonymous;
    private final Long commentId;
    private final Long uploaderId;
    private final String displayName;
    private final String content;

    public static CommentDto fromEntity(Comment comment) {
        if (comment.getUploader() == null) {
            return CommentDto.builder()
                .isAnonymous(true)
                .commentId(comment.getId())
                .uploaderId(-1L)
                .displayName(comment.getDisplayName())
                .content(comment.getContent())
                .build();
        } else {
            return CommentDto.builder()
            .isAnonymous(false)
            .commentId(comment.getId())
            .uploaderId(comment.getUploader().getId())
            .displayName(comment.getDisplayName())
            .content(comment.getContent())
            .build();
        }
    }
}
