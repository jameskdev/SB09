package org.xm.sb09.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xm.sb09.model.Account;
import org.xm.sb09.model.Content;
import org.xm.sb09.model.ContentAttachment;
import org.xm.sb09.model.dto.ContentSubmitResponse;
import org.xm.sb09.model.dto.ContentUpdateResponse;
import org.xm.sb09.model.dto.AccountCreationResponse;
import org.xm.sb09.model.dto.AccountDto;
import org.xm.sb09.model.dto.AccountInfoResponse;
import org.xm.sb09.model.dto.ContentGetResponse;
import org.xm.sb09.model.dto.ContentSubmitRequest;
import org.xm.sb09.model.repositories.AccountRepository;
import org.xm.sb09.model.repositories.ContentRepository;

@Service
public class ContentService {
    private final ContentRepository entityRepository;
    private final AccountRepository accountRepository;

    public ContentService(ContentRepository entityRepository, AccountRepository accountRepository) {
        this.entityRepository = entityRepository;
        this.accountRepository = accountRepository;
        accountRepository.save(new Account("00000000", "Anonymous"));
    }

    public ContentSubmitResponse submitRequest(ContentSubmitRequest request) {
        Optional<Account> acc = accountRepository.findById(request.getId());
        if (acc.isEmpty()) {
            return new ContentSubmitResponse("request_failed_invalid_account", new Date().toString(), HttpStatus.FORBIDDEN);
        }
        Content saved = new Content(request.getSubject(), request.getContent());
        saved.setUploadedBy(acc.get());
        saved = entityRepository.save(saved);
        return new ContentSubmitResponse(String.valueOf(saved.getId()), String.valueOf(saved.getSubmitTime()), HttpStatus.ACCEPTED);
    }

    public ContentSubmitResponse submitRequest(ContentSubmitRequest request, MultipartFile attachedFile) {
        if (attachedFile == null || attachedFile.isEmpty()) {
            return submitRequest(request);
        }
        try {
            Optional<Account> acc = accountRepository.findById(request.getId());
            if (acc.isEmpty()) {
                return new ContentSubmitResponse("request_failed_invalid_account", new Date().toString(), HttpStatus.FORBIDDEN);
            }
            Content saved = new Content(request.getSubject(), request.getContent());
            saved.addFile(new ContentAttachment(saved, attachedFile.getBytes()));
            saved.setUploadedBy(acc.get());
            saved = entityRepository.save(saved);
            return new ContentSubmitResponse(String.valueOf(saved.getId()), String.valueOf(saved.getSubmitTime()), HttpStatus.ACCEPTED);
        } catch (IOException io) {
            return new ContentSubmitResponse("request_failed", String.valueOf(new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Account getAnonymousAccount() {
        Optional<Account> anonymousAccount = accountRepository.findById(1L);
        if (anonymousAccount.isPresent()) {
            return anonymousAccount.get();
        }
        return accountRepository.save(new Account("00000000", "Anonymous"));
    }

    public AccountCreationResponse handleAccountCreationRequest(String displayName, String identifier) {
        if (accountRepository.existsByIdentifier(identifier)) {
            return new AccountCreationResponse(null, HttpStatus.BAD_REQUEST, "The account identifier already exists!");
        }
        Account createdAccount = createNewAccount(displayName, identifier);
        return new AccountCreationResponse(new AccountDto(createdAccount.getId(), createdAccount.getDisplayName(), createdAccount.getIdentifier()), HttpStatus.ACCEPTED, "Account created!");
    }

    public Account createNewAccount(String displayName, String identifier) {
        return accountRepository.save(new Account(identifier, displayName));
    }

    public AccountInfoResponse getAccountInfo(Long id) {
        Optional<Account> accountToSearch = accountRepository.findById(id);
        if (accountToSearch.isPresent()) {
            return new AccountInfoResponse(accountToSearch.get());
        }
        return new AccountInfoResponse();
    }

    public ContentGetResponse getEntityById(Long searchById) {
        Optional<Content> ret = entityRepository.findById(searchById);
        if (ret.isEmpty()) {
            return new ContentGetResponse(List.of(), HttpStatus.NOT_FOUND, "There was no item that matched with your query!");
        } else {
            return new ContentGetResponse(List.of(ret.get()), HttpStatus.OK, "Query successful.");
        }
    }

    public ContentGetResponse getAllEntities() {
        List<Content> lc = entityRepository.findAll();
        if (lc.isEmpty()) {
            return new ContentGetResponse(List.of(), HttpStatus.NOT_FOUND, "There was no item that matched with your query!");
        } else {
            return new ContentGetResponse(lc, HttpStatus.OK, "Query successful.");
        }
    }

    public ContentGetResponse getContentContainingSubject(String subject) {
        List<Content> lc = entityRepository.findBySubjectContainingIgnoreCase(subject);
        if (lc.isEmpty()) {
            return new ContentGetResponse(List.of(), HttpStatus.NOT_FOUND, "There was no item that matched with your query!");
        } else {
            return new ContentGetResponse(lc, HttpStatus.OK, "Query successful.");
        }
    }

    public ContentUpdateResponse deleteEntity(Long id) {
        try {
            entityRepository.deleteById(id);
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.OK, "Delete successful.");
        } catch (IllegalArgumentException ex) {
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.NOT_FOUND, "The item to delete was not found!");
        }
    }

    public ContentUpdateResponse updateEntity(Long id, ContentSubmitRequest req) {
        Optional<Content> res = entityRepository.findById(id);
        if (res.isEmpty()) {
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.NOT_FOUND, "The item to update was not found!");
        }
        Content cnt = res.get();
        try {
            cnt.setSubject(req.getSubject());
            cnt.setContent(req.getContent());
            entityRepository.save(cnt);
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.OK, "Update successful for the content ID: " + id);
        } catch (IllegalArgumentException ex) {
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.NOT_FOUND, "The item to update(" + id + ") was not found!");
        }
    }
    
}
