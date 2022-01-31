package com.security.book.controller.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.List;


@Data
public class BookDto {

    public interface WithId {}
    public interface Basic extends WithId {}
    public interface WithComment extends Basic {}

    @JsonView(WithId.class)
    private Long id;

    @JsonView(Basic.class)
    private String title;

    @JsonView(WithComment.class)
    private String summary;

    @JsonView(WithComment.class)
    private String author;

    @JsonView(WithComment.class)
    private String editorial;

    @JsonView(WithComment.class)
    private int publishYear;

    @JsonView(WithComment.class)
    private List<CommentDto> comments;

    @Override
    public String toString() {
        return String.format("Book [id=%s, title=%s, summary=%s, author=%s, editorial=%s, publishYear=%d", id, title,
                summary, author, editorial, publishYear);
    }
}
