package org.xm.sb09.model.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xm.sb09.model.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
    Optional<Account> findByIdentifier(String identifier);
    Optional<Account> findByDisplayName(String displayName);
    Optional<Account> findByEmail(String email);
    boolean existsByIdentifier(String identifier);
}
