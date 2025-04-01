package org.xm.sb09.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xm.sb09.model.Content;
import org.xm.sb09.model.ContentAttachment;
import org.xm.sb09.model.dto.ContentSubmitResponse;
import org.xm.sb09.model.dto.ContentGetResponse;
import org.xm.sb09.model.dto.ContentSubmitRequest;
import org.xm.sb09.model.repositories.ContentRepository;

@Service
public class ContentService {
    private final ContentRepository entityRepository;

    public ContentService(ContentRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    public ContentSubmitResponse submitRequest(ContentSubmitRequest request) {
        Content saved = entityRepository.save(new Content(request.getSubject(), request.getContent()));
        return new ContentSubmitResponse(String.valueOf(saved.getId()), String.valueOf(saved.getSubmitTime()), HttpStatus.ACCEPTED);
    }

    public ContentSubmitResponse submitRequest(ContentSubmitRequest request, MultipartFile attachedFile) {
        if (attachedFile == null || attachedFile.isEmpty()) {
            return submitRequest(request);
        }
        try {
            Content saved = new Content(request.getSubject(), request.getContent());
            saved.addFile(new ContentAttachment(saved, attachedFile.getBytes()));
            saved = entityRepository.save(saved);
            return new ContentSubmitResponse(String.valueOf(saved.getId()), String.valueOf(saved.getSubmitTime()), HttpStatus.ACCEPTED);
        } catch (IOException io) {
            return new ContentSubmitResponse("request_failed", String.valueOf(new Date()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    
}
