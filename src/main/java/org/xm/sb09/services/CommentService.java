package org.xm.sb09.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xm.sb09.model.Account;
import org.xm.sb09.model.Comment;
import org.xm.sb09.model.Content;
import org.xm.sb09.model.dto.CommentAnonymousPostRequest;
import org.xm.sb09.model.dto.CommentDeleteRequest;
import org.xm.sb09.model.dto.CommentDto;
import org.xm.sb09.model.dto.CommentEditRequest;
import org.xm.sb09.model.dto.CommentGetResponse;
import org.xm.sb09.model.dto.CommentPostRequest;
import org.xm.sb09.model.dto.CommentPostResponse;
import org.xm.sb09.model.dto.CommentUpdateResponse;
import org.xm.sb09.model.repositories.AccountRepository;
import org.xm.sb09.model.repositories.CommentRepository;
import org.xm.sb09.model.repositories.ContentRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final AccountRepository accountRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;


    public CommentGetResponse getSingleComment(Long commentId) {
        Optional<Comment> commentQuery = commentRepository.findById(commentId);
        if (commentQuery.isEmpty()) {
            return new CommentGetResponse(List.of(), "There was no comment with the ID: " + commentId, HttpStatus.NOT_FOUND);
        }
        return new CommentGetResponse(List.of(commentQuery.get()), 1 + " comments found!", HttpStatus.OK);
    }

    public CommentGetResponse getCommentsForContent(Long contentId) {
        Optional<Content> contentQuery = contentRepository.findById(contentId);
        if (contentQuery.isEmpty()) {
            return new CommentGetResponse(List.of(), "There was no content with the ID: " + contentId, HttpStatus.NOT_FOUND);
        }
        List<Comment> commentList = commentRepository.findAllByPostContent(contentQuery.get());
        return new CommentGetResponse(commentList, commentList.size() + " comments found!", HttpStatus.OK);
    }

    public CommentGetResponse getCommentsByUser(Long accountId) {
        Optional<Account> accountQuery = accountRepository.findById(accountId);
        if (accountQuery.isEmpty()) {
            return new CommentGetResponse(List.of(), "There was no user with the ID: " + accountId, HttpStatus.NOT_FOUND);
        }
        List<Comment> commentList = commentRepository.findAllByUploader(accountQuery.get());
        return new CommentGetResponse(commentList, commentList.size() + " comments found!", HttpStatus.OK);
    }

    public CommentPostResponse postAnonymousComment(CommentAnonymousPostRequest request) {
        Optional<Content> contentQuery = contentRepository.findById(request.getPostId());
        if (contentQuery.isEmpty()) {
            return new CommentPostResponse(null, 
                "You are trying to comment on a non-existing content: " + request.getPostId(),
                HttpStatus.NOT_FOUND);
        }
        Comment c = commentRepository.save(Comment.builder().
            uploader(null).
            content(request.getContent()).
            postContent(contentQuery.get()).
            displayName(request.getDisplayName()).
            editPassword(encoder.encode(request.getPassword())).
            build());
        return new CommentPostResponse(CommentDto.fromEntity(c), "Comment post successful", HttpStatus.CREATED);
    }

    public CommentUpdateResponse editComment(CommentEditRequest request) {
        Optional<Comment> commentQuery = commentRepository.findById(request.getCommentId());
        if (commentQuery.isEmpty()) {
            return new CommentUpdateResponse(-1L, 
                "You are trying to edit a non-existing comment: " + request.getCommentId(),
                HttpStatus.NOT_FOUND);
        }
        Comment c = commentQuery.get();
        if (c.getUploader() != null) {
            Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
            if (authInfo.isAuthenticated()) {
                if (authInfo.getPrincipal() instanceof Account p) {
                    if (p.getId() == c.getUploader().getId()) {
                        c.changeContent(request.getContent());
                        commentRepository.save(c);
                        return new CommentUpdateResponse(c.getId(), "Comment successfully edited", HttpStatus.OK);
                    } else {
                        return new CommentUpdateResponse(c.getId(), "You are attempting to edit a comment posted by another user!",  HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    return new CommentUpdateResponse(-1L, 
                        "An internal server error occurred while editing the comment! (Invalid account data in context)",
                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new CommentUpdateResponse(c.getId(), "You are attempting to edit a comment posted by a member without logging in!",  HttpStatus.UNAUTHORIZED);
            }
        } else {
            if (encoder.matches(request.getPassword(), c.getEditPassword())) {
                c.changeContent(request.getContent());
                commentRepository.save(c);
                return new CommentUpdateResponse(c.getId(), "Comment successfully edited", HttpStatus.OK);
            } else {
                return new CommentUpdateResponse(c.getId(), "The password that you provided did not match with the password set for this comment!",  HttpStatus.UNAUTHORIZED);
            }
        }
    }

    public CommentUpdateResponse deleteComment(CommentDeleteRequest request) {
        Optional<Comment> commentQuery = commentRepository.findById(request.getCommentId());
        if (commentQuery.isEmpty()) {
            return new CommentUpdateResponse(-1L, 
                "You are trying to delete a non-existing comment: " + request.getCommentId(),
                HttpStatus.NOT_FOUND);
        }
        Comment c = commentQuery.get();
        if (c.getUploader() != null) {
            Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
            if (authInfo.isAuthenticated()) {
                if (authInfo.getPrincipal() instanceof Account p) {
                    if (p.getId() == c.getUploader().getId()) {
                        commentRepository.delete(c);
                        return new CommentUpdateResponse(c.getId(), "Comment successfully deleted", HttpStatus.OK);
                    } else {
                        return new CommentUpdateResponse(c.getId(), "You are attempting to delete a comment posted by another user!",  HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    return new CommentUpdateResponse(-1L, 
                        "An internal server error occurred while deleting the comment! (Invalid account data in context)",
                        HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new CommentUpdateResponse(c.getId(), "You are attempting to delete a comment posted by a member without logging in!",  HttpStatus.UNAUTHORIZED);
            }
        } else {
            if (encoder.matches(request.getPassword(), c.getEditPassword())) {
                commentRepository.delete(c);
                return new CommentUpdateResponse(c.getId(), "Comment successfully deleted", HttpStatus.OK);
            } else {
                return new CommentUpdateResponse(c.getId(), "The password that you provided did not match with the password set for this comment!",  HttpStatus.UNAUTHORIZED);
            }
        }
    }

    public CommentPostResponse postComment(CommentPostRequest request) {
        Authentication authInfo = SecurityContextHolder.getContext().getAuthentication();
        Optional<Content> contentQuery = contentRepository.findById(request.getPostId());
        if (contentQuery.isEmpty()) {
            return new CommentPostResponse(null, 
                "You are trying to comment on a non-existing content: " + request.getPostId(),
                HttpStatus.NOT_FOUND);
        }
        if (authInfo.isAuthenticated()) {
            return new CommentPostResponse(null, 
                "You are attempting to post a comment as a registered user without logging in! Please post as an anonymous user.",
                HttpStatus.FORBIDDEN);
        }
        if (authInfo.getPrincipal() instanceof Account p) {
            Comment c = commentRepository.save(Comment.builder().
                uploader(p).
                content(request.getContent()).
                postContent(contentQuery.get()).
                displayName("").
                editPassword("").
                build());
            return new CommentPostResponse(CommentDto.fromEntity(c), "Comment post successful", HttpStatus.CREATED);
        } else {
            return new CommentPostResponse(null, 
                "An internal server error occurred while saving the comment! (Invalid account data in context)",
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
