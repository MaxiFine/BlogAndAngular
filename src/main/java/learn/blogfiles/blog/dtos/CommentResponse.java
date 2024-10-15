package learn.blogfiles.blog.dtos;

import lombok.Builder;

import java.time.LocalDate;


@Builder
public record CommentResponse(
        String commentId,
        String commentText,
        LocalDate createdAt,
        String postedBy
) {}
