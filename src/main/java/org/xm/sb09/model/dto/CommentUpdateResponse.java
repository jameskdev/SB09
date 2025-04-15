package org.xm.sb09.model.dto;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentUpdateResponse {
    private final Long commentId;
    private final String message;
    private final HttpStatus responseCode;
}