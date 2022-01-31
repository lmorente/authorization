package com.security.book.service;

import com.security.book.controller.dto.BookDto;
import com.security.book.controller.dto.CommentDto;
import com.security.book.repository.entity.Book;
import com.security.book.repository.entity.Comment;
import com.security.book.repository.BookRepository;
import com.security.book.repository.CommentRepository;
import com.security.book.transformer.BookMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

	private final BookRepository bookRepository;

	private final CommentRepository commentRepository;

	private final BookMapper mapper;

	public BookService(BookRepository books, CommentRepository comments, BookMapper mapper) {
		this.bookRepository = books;
		this.commentRepository = comments;
		this.mapper = mapper;
	}

	public List<BookDto> findAll() {
		return this.mapper.toDtos(this.bookRepository.findAll());
	}

	public Optional<BookDto> findById(Long id) {
		final Optional<Book> bookMO = this.bookRepository.findById(id);
		return bookMO.map(this.mapper::toDto);
	}

	public void save(Book book) {
		this.bookRepository.save(book);
	}

	public void deleteById(Long id) {
		this.bookRepository.deleteById(id);
	}

	public void saveComment(Comment comment, Long bookId) {
		final Book book = this.bookRepository.findById(bookId).orElseThrow();
		book.addComment(comment);
		this.commentRepository.save(comment);
		this.bookRepository.save(book);
	}

	public void deleteCommentById(Long bookId, Long id) {
		final Book book = this.bookRepository.findById(bookId).orElseThrow();
		final Comment deleteComment = book.getComments().stream().filter(p -> p.getId().equals(id)).findFirst().orElseThrow();
		book.removeComment(deleteComment);
		this.bookRepository.save(book);
	}

	public Optional<CommentDto> findCommentById(Long idComment) {
		final Optional<Comment> commentMO = this.commentRepository.findById(idComment);
		return commentMO.map(this.mapper::toDto);
	}
}
