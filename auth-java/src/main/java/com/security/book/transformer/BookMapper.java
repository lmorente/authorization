package com.security.book.transformer;

import com.security.book.controller.dto.BookDto;
import com.security.book.controller.dto.CommentDto;
import com.security.book.repository.entity.Book;
import com.security.book.repository.entity.Comment;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    List<BookDto> toDtos(List<Book> books);

    BookDto toDto(Book book);

    CommentDto toDto(Comment comment);
}
