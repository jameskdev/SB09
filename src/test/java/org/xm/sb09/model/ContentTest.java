package org.xm.sb09.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.xm.sb09.model.repositories.ContentRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
public class ContentTest {
    @Autowired
    ContentRepository cr;

    @Test
    @Transactional
    void testGetContent() {
        String subjectOne = "This is a test";
        String contentOne = "A test content";
        String subjectTwo = "This is a second test";
        String contentTwo = "A second test content";
        Content cts = new Content(subjectOne, contentOne);
        Content cts2 = new Content(subjectTwo, contentTwo);
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("Test");
        
        byte[] testBytes = {0x00, 0x01, 0x02,0x04, 0x08};
        cts.addFile(new ContentAttachment(cts, testBytes));
        cts = cr.save(cts);
        cts2.addFile(new ContentAttachment(cts2, testBytes));
        cts2 = cr.save(cts2);


        assertTrue(subjectOne.equals(cr.findById(1L).get().getSubject()));
        assertTrue(contentOne.equals(cr.findById(1L).get().getContent()));
        assertTrue(subjectTwo.equals(cr.findById(2L).get().getSubject()));
        assertTrue(contentTwo.equals(cr.findById(2L).get().getContent()));
        assertTrue(cr.findById(1L).get().getUploadedFiles().get(0).getAttachmentFile()[0] == 0x00);
        log.info(cr.findById(1L).get().getContent());
        log.info(cr.findById(2L).get().getContent());
        log.info(cr.findById(1L).get().getUploadedFiles().get(0).getAttachmentFile().toString());
        log.info(cr.findById(2L).get().getUploadedFiles().get(0).getAttachmentFile().toString());
    }
}
