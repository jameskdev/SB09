package org.xm.sb09.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.xm.sb09.model.ContentAttachment;

public interface ContentAttachmentRepository extends JpaRepository<ContentAttachment, Long> {
    
}
