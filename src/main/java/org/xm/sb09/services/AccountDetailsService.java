package org.xm.sb09.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xm.sb09.model.repositories.AccountRepository;

@Service
public class AccountDetailsService implements UserDetailsService {
    private final AccountRepository ar;

    public AccountDetailsService(AccountRepository ar) {
        this.ar = ar;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return ar.findByIdentifier(username).orElseThrow(() -> {
            return new UsernameNotFoundException(new StringBuilder().append("Provided username: \"").append(username).append("\" does not exist!").toString());
        });
    }
    
}
