package learn.blogfiles.blog.service;

import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {

    String createBlog(BlogDto blogDto);

    Page<BlogDtoResponse> getAllBlogs(Pageable pageable);

    BlogDtoResponse getBlogDetails(String blogId);

    BlogDtoResponse updateBlog(BlogDto dto, String blogId);

    void likePost(String postId);

}
