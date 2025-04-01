package org.xm.sb09.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xm.sb09.model.Content;

public interface ContentRepository extends JpaRepository<Content, Long> {
    Optional<Content> findById(Long id);
}