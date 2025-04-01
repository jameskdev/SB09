package org.xm.sb09.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xm.sb09.model.dto.ContentGetResponse;
import org.xm.sb09.model.dto.ContentSubmitRequest;
import org.xm.sb09.model.dto.ContentSubmitResponse;
import org.xm.sb09.services.ContentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class WebController {
    private final ContentService entityService;

    public WebController(ContentService entityService) {
        this.entityService = entityService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ContentSubmitResponse> submitContent(@RequestPart(name = "request") ContentSubmitRequest debugRequest, @RequestPart(name = "file", required = false) MultipartFile uploadFile) {
        ContentSubmitResponse res = entityService.submitRequest(debugRequest, uploadFile);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @PostMapping(path = "/submit_get")
    public ResponseEntity<ContentSubmitResponse> submitContentGet(@RequestPart(name = "subject") String subject, @RequestPart(name = "content") String content,  @RequestPart(name = "file", required = false) MultipartFile uploadFile) {
        ContentSubmitResponse res = entityService.submitRequest(new ContentSubmitRequest(subject, content), uploadFile);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/get_stored_content")
    public ResponseEntity<ContentGetResponse> getStoredContent(@RequestParam(name="submit_id") Long searchById) {
        ContentGetResponse res = entityService.getEntityById(searchById);
        return new ResponseEntity<>(res, res.getResponseCode());
    }

    @GetMapping("/get_all_contents")
    public ResponseEntity<ContentGetResponse> getStoredContent() {
        ContentGetResponse res = entityService.getAllEntities();
        return new ResponseEntity<>(res, res.getResponseCode());
    }
}
