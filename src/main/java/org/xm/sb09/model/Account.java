package org.xm.sb09.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String identifier;
    private String displayName;

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
}
