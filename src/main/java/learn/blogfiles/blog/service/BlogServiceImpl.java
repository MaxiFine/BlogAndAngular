package learn.blogfiles.blog.service;


import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import learn.blogfiles.blog.handlers.NotFound404Exception;
import learn.blogfiles.blog.mappers.BlogMapper;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService{

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;


    @Override
    public String createBlog(BlogDto blogDto) {
      BlogEntity blog = blogMapper.mapToBlogEntity(blogDto);
//      BlogDtoResponse response = blogMapper.mapBlogDtoResponse(blog);
       blogRepository.save(blog);
       return "Blog created Successfully... id is: " + blog.getBlogId();
    }


    @Override
    public Page<BlogDtoResponse> getAllBlogs(Pageable pageable) {
        Page<BlogEntity> blogs = blogRepository.findAll(pageable);
        List<BlogDtoResponse> allBlogResponse = blogs.stream()
                .map(blogMapper::mapBlogDtoResponse)
                .toList();
        return new PageImpl<>(allBlogResponse);
    }


    @Override
    public BlogDtoResponse getBlogDetails(String blogId) {
        Optional<BlogEntity> optionalBlog = blogRepository.findById(blogId);
        if (optionalBlog.isEmpty()) {
            throw new NotFound404Exception("The blogId does not exists");
        }else {
            BlogEntity blog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new NotFound404Exception("The blogId does not exists"));
            return blogMapper.mapBlogDtoResponse(blog);
        }
    }


    @Override
    public BlogDtoResponse getBlogNameContent(String name) {
        Optional<BlogEntity> optionalBlog = Optional.ofNullable(blogRepository.byName(name));
        if (optionalBlog.isEmpty()) {
            throw new NotFound404Exception("The blogId does not exists");
        }else {
            BlogEntity blog = blogRepository.byName(name);
//                    .orElseThrow(() -> new NotFound404Exception("The blogId does not exists"));
            return blogMapper.mapBlogDtoResponse(blog);
        }
    }


    @Override
    public BlogDtoResponse updateBlog(BlogDto dto, String blogId) {
        BlogEntity optionalBlog = blogRepository.findById(blogId)
                    .orElseThrow(() -> new NotFound404Exception("The blogId does not exists"));
        return updaterBlog(dto, optionalBlog);
    }


    private BlogDtoResponse updaterBlog(BlogDto blogDto, BlogEntity blog){
        blog.setName(blogDto.name());
        blog.setContent(blogDto.content());
        blog.setImage(blogDto.image());
        blog.setBlogTags(blogDto.blogTags());
       BlogEntity updatedBlog = blogRepository.save(blog);
        return blogMapper.mapBlogDtoResponse(updatedBlog);
    }


    @Override
    public void likePost(String postId) {
        BlogEntity blog = blogRepository.findById(postId).orElseThrow(
                () -> new NotFound404Exception("The blog with ID " + postId + " was not found."));
        blog.setLikeCounts(blog.getLikeCounts() + 1);
        System.out.println(blog.getLikeCounts());
        blogRepository.save(blog);
    }
}
