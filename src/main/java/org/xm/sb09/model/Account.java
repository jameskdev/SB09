package org.xm.sb09.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private String displayName;
    @OneToMany(mappedBy = "uploadedBy")
    List<Content> uploadedContents = new ArrayList<>();

    protected Account() {
        identifier = "";
        displayName = "";
    }

    public Account(String identifier, String displayName) {
        this.identifier = identifier;
        this.displayName = displayName;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Long getId() {
        return this.id;
    }

    public List<Content> getUploadedContents() {
        return Collections.unmodifiableList(uploadedContents);
    }
 }
