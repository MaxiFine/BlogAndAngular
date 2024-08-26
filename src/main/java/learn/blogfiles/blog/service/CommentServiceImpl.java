package learn.blogfiles.blog.service;

import learn.blogfiles.blog.dtos.CommentDto;
import learn.blogfiles.blog.handlers.NotFound404Exception;
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


    @Override
    public Comment createComment(String blogId, String postedBy, String content) {
        BlogEntity blog = blogRepository.findById(blogId).orElseThrow(
                () -> new NotFound404Exception("Blog with ID " + blogId +" does not exist."));
        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setCommentText(content);
        comment.setPostedBy(postedBy);
        comment.setCreatedAt(LocalDate.now());
        return commentRepository.save(comment);
    }
}
