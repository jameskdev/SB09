package org.xm.sb09.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountDto {
    private final Long id;
    private final String displayName;
    private final String identifier;
}