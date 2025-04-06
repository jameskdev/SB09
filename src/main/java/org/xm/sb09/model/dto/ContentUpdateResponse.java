package org.xm.sb09.model.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContentUpdateResponse {
    private final String id;
    private final HttpStatus responseCode;
    private final String responseMessage;
}
