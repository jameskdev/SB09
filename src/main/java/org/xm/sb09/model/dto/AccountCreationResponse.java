package org.xm.sb09.model.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountCreationResponse {
    private final AccountDto createdAccount;
    private final HttpStatus responseCode;
    private final String message;
}
