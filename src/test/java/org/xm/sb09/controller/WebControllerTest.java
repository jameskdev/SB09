package org.xm.sb09.controller;

import org.xm.sb09.model.Account;
import org.xm.sb09.model.Content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.xm.sb09.model.ContentAttachment;
import org.xm.sb09.model.dto.ContentUpdateRequest;
import org.xm.sb09.model.repositories.AccountRepository;
import org.xm.sb09.model.repositories.ContentRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.*;
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
    @Autowired
    private AccountRepository ar;
    @Autowired
    private ObjectMapper ojm;

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
        Account anonyous = ar.save(new Account("00000000", "Anonymous"));
        Content cts = Content.builder().subject(subjectOne).content(contentOne).build();
        Content cts2 = Content.builder().subject(subjectTwo).content(contentTwo).build();
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger("Test");
        
        byte[] testBytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        cts.addFile(new ContentAttachment(cts, testBytes));
        cts.setUploadedBy(anonyous);
        cts = cr.save(cts);
        cts2.addFile(new ContentAttachment(cts2, testBytes));
        cts2.setUploadedBy(anonyous);
        cts2 = cr.save(cts2);
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/all_contents"));
        ra.andExpect(status().isOk()) // Check that the response is HTTP 200 OK
        .andExpect(jsonPath("$.results[0].subject").value(subjectOne))
        .andExpect(jsonPath("$.results[0].content").value(contentOne))
        .andExpect(jsonPath("$.results[0].attachments[0].attachmentFile").value(Base64.getEncoder().encodeToString(testBytes)))
        .andExpect(jsonPath("$.results[0].uploadedBy.displayName").value(anonyous.getDisplayName()))
        .andExpect(jsonPath("$.results[0].uploadedBy.identifier").value(anonyous.getIdentifier()))
        .andExpect(jsonPath("$.results[0].uploadedBy.id").value(anonyous.getId()))
        .andExpect(jsonPath("$.results[1].subject").value(subjectTwo))
        .andExpect(jsonPath("$.results[1].content").value(contentTwo))
        .andExpect(jsonPath("$.results[1].attachments[0].attachmentFile").value(Base64.getEncoder().encodeToString(testBytes)))
        .andExpect(jsonPath("$.results[1].uploadedBy.displayName").value(anonyous.getDisplayName()))
        .andExpect(jsonPath("$.results[1].uploadedBy.identifier").value(anonyous.getIdentifier()))
        .andExpect(jsonPath("$.results[1].uploadedBy.id").value(anonyous.getId()));
    }

    @Test
    void testSingleContent() throws Exception {
        // Given
        String subject = "This is a test";
        String content = "A test content";
        byte[] testBytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        Account anonyous = new Account("00000000", "Anonymous");

        // When
        anonyous = ar.save(anonyous);
        Content cts = Content.builder().subject(subject).content(content).build();
        cts.addFile(new ContentAttachment(cts, testBytes));
        cts.setUploadedBy(anonyous);
        cts = cr.save(cts);
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/content/{id}", cts.getId()));

        // Then
        ra.andExpect(status().isOk()) // Check that the response is HTTP 200 OK 
        .andExpect(jsonPath("$.results[0].subject").value(subject))
        .andExpect(jsonPath("$.results[0].content").value(content))
        .andExpect(jsonPath("$.results[0].attachments[0].attachmentFile").value(Base64.getEncoder().encodeToString(testBytes)))
        .andExpect(jsonPath("$.results[0].uploadedBy.displayName").value(anonyous.getDisplayName()))
        .andExpect(jsonPath("$.results[0].uploadedBy.identifier").value(anonyous.getIdentifier()))
        .andExpect(jsonPath("$.results[0].uploadedBy.id").value(anonyous.getId()));
    }

    @Test
    void testDeleteContent() throws Exception {
        // Given
        String subject = "This is a test";
        String content = "A test content";
        byte[] testBytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        Account anonyous = new Account("00000000", "Anonymous");
        anonyous = ar.save(anonyous);
        Content cts = Content.builder().subject(subject).content(content).build();
        cts.addFile(new ContentAttachment(cts, testBytes));
        cts.setUploadedBy(anonyous);
        cts = cr.save(cts);

        // When
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.delete("/content/{id}", cts.getId()));

        // Then
        ra.andExpect(status().isOk()); // Check that the response is HTTP 200 OK 
        assertThat(cr.findById(cts.getId()).isEmpty()).isTrue();
    }

    @Test
    void testUpdateContent() throws Exception {
        // Given
        String subject = "This is a test";
        String content = "A test content";
        Account anonyous = new Account("00000000", "Anonymous");
        anonyous = ar.save(anonyous);
        Content cts = Content.builder().subject(subject).content(content).build();
        cts.setUploadedBy(anonyous);
        cts = cr.save(cts);
        ContentUpdateRequest updateRequest = new ContentUpdateRequest("Updated content subject", "Updated content", anonyous.getId());

        // When
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders
            .multipart(HttpMethod.PUT, "/content/{id}", cts.getId())
            .file(new MockMultipartFile("request", "request", "application/json", ojm.writeValueAsString(updateRequest).getBytes())));
        ResultActions ra_get = mockMvc.perform(MockMvcRequestBuilders.get("/content/{id}", cts.getId()));
        
        // Then
        ra.andExpect(status().isOk()); // Check that the response for the update request is HTTP 200 OK 
        ra_get.andExpect(status().isOk()) // Check that the response for the GET request that fetches the update content is HTTP 200 OK 
        .andExpect(jsonPath("$.results[0].subject").value(updateRequest.getSubject())) // Check that the subject has been updated
        .andExpect(jsonPath("$.results[0].content").value(updateRequest.getContent())); // Check that the content has been updated
    }

    @Test
    void testError404OnNotFound() throws Exception {
        // Given
        String subject = "This is a test";
        String content = "A test content";
        Account anonyous = new Account("00000000", "Anonymous");
        anonyous = ar.save(anonyous);
        Content cts = Content.builder().subject(subject).content(content).build();
        cts.setUploadedBy(anonyous);
        cts = cr.save(cts);
        ContentUpdateRequest updateRequest = new ContentUpdateRequest("Updated content subject", "Updated content", anonyous.getId());

        // When
        ResultActions ra = mockMvc.perform(MockMvcRequestBuilders
            .multipart(HttpMethod.PUT, "/content/{id}", 9999L) // Set the requested content ID to 9999L (which is invalid)
            .file(new MockMultipartFile("request", "request", "application/json", ojm.writeValueAsString(updateRequest).getBytes())));
        
        // Then
        ra.andExpect(status().isNotFound()) //This should result in 404 not found
        .andExpect(jsonPath("$.id").value(9999L)); // Check that the ID in the error message is same as the requested content ID (9999L)
    }
}
