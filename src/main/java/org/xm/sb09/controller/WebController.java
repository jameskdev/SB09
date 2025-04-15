package org.xm.sb09.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xm.sb09.model.dto.AccountInfoResponse;
import org.xm.sb09.model.dto.CommentAnonymousPostRequest;
import org.xm.sb09.model.dto.CommentDeleteRequest;
import org.xm.sb09.model.dto.CommentEditRequest;
import org.xm.sb09.model.dto.CommentGetResponse;
import org.xm.sb09.model.dto.CommentPostRequest;
import org.xm.sb09.model.dto.CommentPostResponse;
import org.xm.sb09.model.dto.CommentUpdateResponse;
import org.xm.sb09.model.dto.ContentGetResponse;
import org.xm.sb09.model.dto.ContentSubmitRequest;
import org.xm.sb09.model.dto.ContentSubmitResponse;
import org.xm.sb09.model.dto.ContentUpdateRequest;
import org.xm.sb09.model.dto.ContentUpdateResponse;
import org.xm.sb09.services.CommentService;
import org.xm.sb09.services.ContentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WebController {
    private final ContentService entityService;
    private final CommentService commentService;

    public WebController(ContentService entityService, CommentService commentService) {
        this.entityService = entityService;
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public ResponseEntity<CommentPostResponse> postComment(@RequestBody CommentPostRequest request) {
        CommentPostResponse res = commentService.postComment(request);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @DeleteMapping("/comment")
    public ResponseEntity<CommentUpdateResponse> deleteComment(@RequestBody CommentDeleteRequest request) {
        CommentUpdateResponse res = commentService.deleteComment(request);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @PostMapping("/comment/anonymous")
    public ResponseEntity<CommentPostResponse> postAnonymousComment(@RequestBody CommentAnonymousPostRequest request) {
        CommentPostResponse res = commentService.postAnonymousComment(request);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/comment/content/{id}")
    public ResponseEntity<CommentGetResponse> getCommentsForContent(@PathVariable(name="id") Long contentId) {
        CommentGetResponse res = commentService.getCommentsForContent(contentId);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/comment/user/{id}")
    public ResponseEntity<CommentGetResponse> getCommentsByUser(@PathVariable(name="id") Long userId) {
        CommentGetResponse res = commentService.getCommentsByUser(userId);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @PostMapping("/content")
    public ResponseEntity<ContentSubmitResponse> submitContent(@RequestPart(name = "request") ContentSubmitRequest debugRequest, @RequestPart(name = "file", required = false) List<MultipartFile> uploadFile) {
        ContentSubmitResponse res = entityService.submitRequest(debugRequest, uploadFile);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @PostMapping(path = "/content_formdata")
    public ResponseEntity<ContentSubmitResponse> submitContentGet(@RequestPart(name = "subject") String subject, @RequestPart(name = "content") String content, @RequestPart(name = "id", required = false) Long id,  @RequestPart(name = "file", required = false) List<MultipartFile> uploadFile) {
        ContentSubmitResponse res = entityService.submitRequest(new ContentSubmitRequest(subject, content, id), uploadFile);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/content/{id}")
    public ResponseEntity<ContentGetResponse> getStoredContent(@PathVariable(name="id") Long searchById) {
        ContentGetResponse res = entityService.getEntityById(searchById);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<AccountInfoResponse> getAccountInfo(@PathVariable(name="id") Long searchById) {
        AccountInfoResponse res = entityService.getAccountInfo(searchById);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/content")
    public ResponseEntity<ContentGetResponse> getContentsContainingSubject(@RequestParam(name="subject") String subject) {
        ContentGetResponse res = entityService.getContentContainingSubject(subject);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @PutMapping("/content/{id}")
    public ResponseEntity<ContentUpdateResponse> updateContent(@PathVariable(name="id") Long searchById, @RequestPart(name = "request") ContentUpdateRequest request, @RequestPart(name = "file", required = false) List<MultipartFile> uploadFile) {
        ContentUpdateResponse res = entityService.updateEntity(searchById, request);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @DeleteMapping("/content/{id}")
    public ResponseEntity<ContentUpdateResponse> deleteContent(@PathVariable(name="id") Long searchById) {
        ContentUpdateResponse res = entityService.deleteEntity(searchById);
            return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/all_contents")
    public ResponseEntity<ContentGetResponse> getStoredContent() {
        ContentGetResponse res = entityService.getAllEntities();
        return new ResponseEntity<>(res, res.getResponseCode());
    }
}
