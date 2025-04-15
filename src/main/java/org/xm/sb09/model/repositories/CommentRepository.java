package org.xm.sb09.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xm.sb09.model.Account;
import org.xm.sb09.model.Comment;
import org.xm.sb09.model.Content;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostContent(Content postContent);
    List<Comment> findAllByUploader(Account uploader);
}
