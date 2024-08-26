package learn.blogfiles.blog.service;

import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.responses.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {

    BlogDtoResponse createBlog(BlogDto blogDto);

    Page<BlogDtoResponse> getAllBlogs(Pageable pageable);

    BlogDtoResponse getBlogDetails(String blogId);

    BlogDtoResponse updateBlog(BlogDto dto, String blogId);

    void likePost(String postId);

}
