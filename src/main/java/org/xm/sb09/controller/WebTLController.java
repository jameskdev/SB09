package org.xm.sb09.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.xm.sb09.model.dto.ContentDto;
import org.xm.sb09.model.dto.ContentGetResponse;
import org.xm.sb09.model.dto.ContentTLDto;
import org.xm.sb09.model.repositories.ContentRepository;
import org.xm.sb09.services.ContentService;

import lombok.RequiredArgsConstructor;


// Controller for the basic Thymeleaf front-end.
@Controller
@RequiredArgsConstructor
public class WebTLController {
    private final ContentService service;

    @GetMapping("/posts/{id}")
    public String getSinglePost(@PathVariable("id") Long id, Model model) {
        ContentGetResponse res = service.getEntityById(id);
        if (res.getResponseCode() != HttpStatus.OK) {
            return "error-page";
        }
        ContentDto cnt = res.getResults().getFirst();
        ContentTLDto content =
        ContentTLDto.builder()
            .id(cnt.getId())
            .subject(cnt.getSubject())
            .content(cnt.getContent())
            .uploadedDate(cnt.getUploadedDate().toString())
            .uploader(cnt.getUploadedBy().getDisplayName())
            .build();

        cnt.getAttachments().forEach(x -> content.addAttachmentId(x.getId()));
        model.addAttribute("contentdto", content);
        model.addAttribute("today", LocalDateTime.now());
 
        return "content-page";
    }

    @GetMapping("/posts/all")
    public String getAllPosts(Model model) {
        ContentGetResponse res = service.getAllEntities();
        if (res.getResponseCode() != HttpStatus.OK) {
            return "error-page";
        }
        List<ContentDto> cntList = res.getResults();
        List<ContentTLDto> cntDtoList = new ArrayList<>();
        cntList.forEach(cnt -> {
            ContentTLDto tlDto = ContentTLDto.builder()
                .id(cnt.getId())
                .subject(cnt.getSubject())
                .content(cnt.getContent())
                .uploadedDate(cnt.getUploadedDate().toString())
                .uploader(cnt.getUploadedBy().getDisplayName())
                .build();
            cnt.getAttachments().forEach(x -> tlDto.addAttachmentId(x.getId()));
            cntDtoList.add(tlDto);
        });
        model.addAttribute("cntdtolist", cntDtoList);
        model.addAttribute("today", LocalDateTime.now());
 
        return "all-contents";
    }
        
}
