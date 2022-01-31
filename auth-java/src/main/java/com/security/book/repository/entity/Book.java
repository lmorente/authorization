package com.security.book.repository.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Data
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String title;

	@NotBlank
	@Column(columnDefinition = "TEXT")
	private String summary;

	private String author;

	private String editorial;

	private int publishYear;

	@OneToMany(mappedBy="book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Comment> comments;

	public Book() {
	}

	public Book(String title, String summary, String author, String editorial, int publishYear) {
		this(title, summary, author, editorial, publishYear, new ArrayList<>());
	}
	
	public Book(String title, String summary, String author, String editorial, int publishYear, List<Comment> comments) {
		this.title = title;
		this.summary = summary;
		this.author = author;
		this.editorial = editorial;
		this.publishYear = publishYear;		
		this.comments = comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setBook(this);
	}
	
	public void removeComment(Comment comment) {
		boolean removed = comments.remove(comment);
		if(!removed) {
			throw new NoSuchElementException();
		}
		comment.setBook(null);
	}

	@Override
	public String toString() {
		return String.format("Book [id=%s, title=%s, summary=%s, author=%s, editorial=%s, publishYear=%d", id, title,
				summary, author, editorial, publishYear);
	}
}
