package com.security.book.controller.dto;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class CommentDto {

	@JsonView(BookDto.WithComment.class)
	private Long id;

	@JsonView(BookDto.WithComment.class)
	private String content;

	@JsonView(BookDto.WithComment.class)
	private int score;
}
