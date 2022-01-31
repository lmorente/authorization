package com.security.book.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {

	public interface Basic {}
	public interface WithBook {}
	public interface WithUser {}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonView(Basic.class)
	private Long id;

	@NotBlank
	@Column(columnDefinition = "TEXT")
	@JsonView(Basic.class)
	private String content;

	@JsonView(Basic.class)
	private int score;

	@ManyToOne
	@JsonView(WithBook.class)
	private Book book;

	public Comment(int punctuation, String content) {
		this.content = content;
		this.score = punctuation;
	}

	public Comment(Comment comment) {
		this.id = comment.id;
		this.content = comment.content;
		this.score = comment.score;
		this.book = comment.book;
	}
}
