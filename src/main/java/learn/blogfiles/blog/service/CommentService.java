package learn.blogfiles.blog.service;


import learn.blogfiles.blog.dtos.CommentDto;
import learn.blogfiles.blog.dtos.CommentResponse;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {


    CommentResponse createComment(String blogId, CommentDto commentDto);

}
