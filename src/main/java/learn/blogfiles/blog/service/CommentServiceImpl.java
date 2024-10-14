package learn.blogfiles.blog.service;

import learn.blogfiles.blog.dtos.CommentDto;
import learn.blogfiles.blog.dtos.CommentResponse;
import learn.blogfiles.blog.handlers.NotFound404Exception;
import learn.blogfiles.blog.mappers.CommentMapper;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.blog.model.Comment;
import learn.blogfiles.blog.repository.BlogRepository;
import learn.blogfiles.blog.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentResponse createComment(String blogId, CommentDto commentDto) {
        BlogEntity blog = blogRepository.findById(blogId).orElseThrow(
                () -> new NotFound404Exception("Blog with ID " + blogId +" does not exist."));
        Comment comment = commentMapper.toCommentEntity(commentDto, blog);
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

}
