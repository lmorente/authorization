package com.security.book.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.security.book.controller.dto.BookDto;
import com.security.book.controller.dto.CommentDto;
import com.security.book.repository.entity.Comment;
import com.security.book.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
public class CommentController {

    private final BookService bookService;

    public CommentController(BookService books) {
        this.bookService = books;
    }

    interface WithComment extends BookDto.WithId {
    }

    @PostMapping("/books/{id}/comments/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @JsonView(WithComment.class)
    public ResponseEntity<?> createComment(@Valid @RequestBody Comment comment, @PathVariable Long id) {
        bookService.saveComment(comment, id);
        return ResponseEntity.created(fromCurrentRequest().path("/{id}")
                .build(comment.getId())).body(comment);
    }

    @GetMapping(value = "/comment/{idComment}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long idComment) {
        final Optional<CommentDto> comment = this.bookService.findCommentById(idComment);
        if (comment.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(comment.get());
    }

    @JsonView(WithComment.class)
    @DeleteMapping("/books/{bookId}/comments/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteComment(@PathVariable Long bookId, @PathVariable Long id) {
        this.bookService.deleteCommentById(bookId, id);
        return ResponseEntity.ok().build();
    }
}

