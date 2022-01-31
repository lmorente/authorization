package com.security.book.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.security.book.controller.dto.BookDto;
import com.security.book.repository.entity.Book;
import com.security.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/")
    @JsonView(BookDto.Basic.class)
    public Collection<BookDto> getBooks() {
        return this.bookService.findAll();
    }

    //TODO: JsonView + PreAuthorize
    @GetMapping("/books/details")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @JsonView(BookDto.WithComment.class)
    public Collection<BookDto> getBooksDetails() {
        return this.bookService.findAll();
    }

    @GetMapping("/books/{id}")
    @JsonView(BookDto.WithComment.class)
    public BookDto getBook(@PathVariable long id) {
        return this.bookService.findById(id).orElseThrow();
    }

    @PostMapping("/books/")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @JsonView(BookDto.Basic.class)
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        this.bookService.save(book);
        return ResponseEntity.created(fromCurrentRequest().path("/{id}")
                .buildAndExpand(book.getId()).toUri()).body(book);
    }

    @DeleteMapping(value = "/books/{idBook}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteBook(@PathVariable Long idBook) {
        if(this.bookService.findById(idBook).isPresent()){
            this.bookService.deleteById(idBook);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}