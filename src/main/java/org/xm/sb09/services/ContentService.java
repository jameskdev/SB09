package org.xm.sb09.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xm.sb09.model.Account;
import org.xm.sb09.model.Content;
import org.xm.sb09.model.ContentAttachment;
import org.xm.sb09.model.dto.ContentSubmitResponse;
import org.xm.sb09.model.dto.ContentUpdateRequest;
import org.xm.sb09.model.dto.ContentUpdateResponse;
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
    }

    public ContentSubmitResponse submitRequest(ContentSubmitRequest request, List<MultipartFile> attachedFiles) {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
        try {
            Content saved = new Content(request.getSubject(), request.getContent());
            for (MultipartFile attachedFile: attachedFiles) {
                saved.addFile(new ContentAttachment(saved, attachedFile.getBytes()));
            }
            if (authInfo.isAuthenticated()) {
                if (authInfo.getPrincipal() instanceof Account p) {
                    saved.setUploadedBy(p);
                    saved = entityRepository.save(saved);
                    return new ContentSubmitResponse(String.valueOf(saved.getId()), String.valueOf(saved.getSubmitTime()), HttpStatus.CREATED);
                } else {
                    return new ContentSubmitResponse("invalid_authorization_context", String.valueOf(new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                // In order to allow anonymous contents, this needs to be changed.
                return new ContentSubmitResponse("not_logged_in", String.valueOf(new Date()), HttpStatus.FORBIDDEN);
            }
        } catch (IOException io) {
            return new ContentSubmitResponse("request_failed_io_error", String.valueOf(new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException io) {
            return new ContentSubmitResponse("request_failed_illegal_argument", String.valueOf(new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
            Optional<Content> contentQuery = entityRepository.findById(id);
            if (contentQuery.isEmpty()) {
                return new ContentUpdateResponse(String.valueOf(id), HttpStatus.NOT_FOUND, "The item to delete was not found!");
            }
            Content c = contentQuery.get();
            if (c.getUploadedBy() != null) {
                if (authInfo.isAuthenticated()) {
                    if (authInfo.getPrincipal() instanceof Account p) {
                        if (c.getUploadedBy().getId() == p.getId()) {
                            entityRepository.deleteById(id);
                            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.OK, "Delete successful.");
                        } else {
                            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.FORBIDDEN, "You are trying to delete a content posted by another member!.");
                        }
                    } else {
                        return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authorization context information!.");
                    }
                } else {
                    return new ContentUpdateResponse(String.valueOf(id), HttpStatus.FORBIDDEN, "You are trying to delete a content posted by a member without logging in!.");
                }
            } else {
                return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "The content's uploader information is null!.");
                // TBD: Needs to be changed in order to allow anonymous contents.
            }
        } catch (IllegalArgumentException ex) {
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred while deleting the item!");
        }
    }

    public ContentUpdateResponse purgeEntityRepository() {
        entityRepository.deleteAll();
        return new ContentUpdateResponse("", HttpStatus.OK, "Database emptied.");
    }

    // @Transactional
    public ContentUpdateResponse updateEntity(Long id, ContentUpdateRequest req) {
        try {
            Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
            Optional<Content> contentQuery = entityRepository.findById(id);
            if (contentQuery.isEmpty()) {
                return new ContentUpdateResponse(String.valueOf(id), HttpStatus.NOT_FOUND, "The item to update was not found!");
            }
            Content c = contentQuery.get();
            if (c.getUploadedBy() != null) {
                if (authInfo.isAuthenticated()) {
                    if (authInfo.getPrincipal() instanceof Account p) {
                        if (c.getUploadedBy().getId() == p.getId()) {
                            c.setSubject(req.getSubject());
                            c.setContent(req.getContent());
                            entityRepository.save(c);
                            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.OK, "Update successful for the content ID: " + id);
                        } else {
                            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.FORBIDDEN, "You are trying to update a content posted by another member!.");
                        }
                    } else {
                        return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "Invalid authorization context information!.");
                    }
                } else {
                    return new ContentUpdateResponse(String.valueOf(id), HttpStatus.FORBIDDEN, "You are trying to update a content posted by a member without logging in!.");
                }
            } else {
                return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "The content's uploader information is null!.");
                // TBD: Needs to be changed in order to allow anonymous contents.
            }
        } catch (IllegalArgumentException ex) {
            return new ContentUpdateResponse(String.valueOf(id), HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred while updating the item!");
        }
    }
    
}