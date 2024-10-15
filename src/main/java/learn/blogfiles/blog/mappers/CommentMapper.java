package learn.blogfiles.blog.mappers;

import learn.blogfiles.blog.dtos.CommentDto;
import learn.blogfiles.blog.dtos.CommentResponse;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.blog.model.Comment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CommentMapper {

    public Comment toCommentEntity(CommentDto commentDto, BlogEntity blogEntity) {
        return Comment.builder()
                .commentText(commentDto.commentText())
                .postedBy(commentDto.postedBy())
                .createdAt(LocalDate.now())
                .blog(blogEntity)
                .build();
    }


    public CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getCommentId())
                .commentText(comment.getCommentText())
                .createdAt(comment.getCreatedAt())
                .postedBy(comment.getPostedBy())
                .build();
    }
}
