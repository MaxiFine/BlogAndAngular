package learn.blogfiles.blog.dtos;

import java.time.LocalDate;

public record CommentDto(

        String commentText,
        LocalDate createdAt,
        String postedBy
) {}
