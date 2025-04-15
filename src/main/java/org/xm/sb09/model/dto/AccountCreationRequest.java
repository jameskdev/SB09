package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreationRequest {
    private final String displayName;
    private final String identifier;
    private final String email;
    private final String password;

    public AccountCreationRequest(@JsonProperty("display_name") String displayName, @JsonProperty("identifier") String identifier, 
    @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.displayName = displayName;
        this.identifier = identifier;
        this.email = email;
        this.password = password;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }
}
