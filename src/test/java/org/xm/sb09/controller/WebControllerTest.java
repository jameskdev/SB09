package org.xm.sb09.controller;

import org.xm.sb09.model.Content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.xm.sb09.model.ContentAttachment;
import org.xm.sb09.model.repositories.ContentRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Base64;

@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ContentRepository cr;

    @BeforeEach
    public void setupTest() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetStoredContent() throws Exception {
        String subjectOne = "This is a test";
        String contentOne = "A test content";
        String subjectTwo = "This is a second test";
        String contentTwo = "A second test content";
        Content cts = new Content(subjectOne, contentOne);
        Content cts2 = new Content(subjectTwo, contentTwo);
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("Test");
        
        byte[] testBytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        cts.addFile(new ContentAttachment(cts, testBytes));
        cts = cr.save(cts);
        cts2.addFile(new ContentAttachment(cts2, testBytes));
        cts2 = cr.save(cts2);
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/get_all_contents"));
        ra.andExpect(status().isOk()) // Check that the response is HTTP 200 OK
        .andExpect(jsonPath("$.results[0].subject").value(subjectOne))
        .andExpect(jsonPath("$.results[0].content").value(contentOne))
        .andExpect(jsonPath("$.results[0].uploadedFiles[0].attachmentFile").value(Base64.getEncoder().encodeToString(testBytes)))
        .andExpect(jsonPath("$.results[1].subject").value(subjectTwo))
        .andExpect(jsonPath("$.results[1].content").value(contentTwo))
        .andExpect(jsonPath("$.results[1].uploadedFiles[0].attachmentFile").value(Base64.getEncoder().encodeToString(testBytes)));
    }
}
