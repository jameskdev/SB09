package org.xm.sb09.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xm.sb09.model.Account;
import org.xm.sb09.model.dto.AccountCreationRequest;
import org.xm.sb09.model.repositories.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository ar;
    private final BCryptPasswordEncoder bpr;

    public void createAccount(AccountCreationRequest request) {
        this.ar.save(new Account(request.getIdentifier(), request.getDisplayName(), request.getEmail(), bpr.encode(request.getPassword())));
    }
}
