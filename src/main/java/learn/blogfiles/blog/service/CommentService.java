package learn.blogfiles.blog.service;


import learn.blogfiles.blog.model.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    public Comment createComment(String blogId, String postedBy, String content);

}
