package org.xm.sb09.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountCreationRequest {
    private final String displayName;
    private final String identifier;

    public AccountCreationRequest(@JsonProperty("display_name") String displayName, @JsonProperty("identifier") String identifier) {
        this.displayName = displayName;
        this.identifier = identifier;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getIdentifier() {
        return this.identifier;
    }
}
