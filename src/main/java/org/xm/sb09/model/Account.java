package org.xm.sb09.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;

@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private String displayName;
    private String password;
    private String email;
    @OneToMany(mappedBy = "uploadedBy")
    List<Content> uploadedContents = new ArrayList<>();

    protected Account() {
        this.identifier = "";
        this.displayName = "";
        this.email = "";
        this.password = "";
    }

    public Account(String identifier, String displayName) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.email = "";
        this.password = "";
    }


    public Account(String identifier, String displayName, String email, String password) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public Long getId() {
        return this.id;
    }

    public List<Content> getUploadedContents() {
        return Collections.unmodifiableList(uploadedContents);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.identifier;
    }
 }
