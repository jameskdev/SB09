package org.xm.sb09.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.xm.sb09.model.Account;

public class AccountInfoResponse {
    private final AccountDto accountInfo;
    private final List<String> listOfContents;
    private final HttpStatus responseCode;

    public AccountInfoResponse() {
        this.accountInfo = new AccountDto(-1L, "", "");
        listOfContents = new ArrayList<>();
        this.responseCode = HttpStatus.NOT_FOUND;
    }

    public AccountInfoResponse(Account account) {
        this.accountInfo = new AccountDto(account.getId(), account.getDisplayName(), account.getIdentifier());
        listOfContents = new ArrayList<>();
        account.getUploadedContents().forEach(x -> listOfContents.add(x.getSubject()));
        this.responseCode = HttpStatus.OK;
    }

    public HttpStatus getResponseCode() {
        return this.responseCode;
    }

    public AccountDto getAccountInfo() {
        return this.accountInfo;
    }

    public List<String> getListOfContents() {
        return Collections.unmodifiableList(listOfContents);
    }
}
