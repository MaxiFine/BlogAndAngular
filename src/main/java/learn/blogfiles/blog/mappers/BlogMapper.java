package learn.blogfiles.blog.mappers;


import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import learn.blogfiles.blog.model.BlogEntity;
import org.springframework.stereotype.Service;

@Service
public class BlogMapper {

    public BlogEntity mapToBlogEntity(BlogDto blogDto){
        return BlogEntity.builder()
                .name(blogDto.name())  // Use getter for content
                .content(blogDto.content())
                .postedBy(blogDto.postedBy())
                .image(blogDto.image())
                .createdAt(blogDto.createdAt())
                .blogTags(blogDto.blogTags())
                .likeCounts(blogDto.likeCounts())
                .viewCounts(blogDto.viewCount())
                .published(true)
                .build();
    }

    public BlogDtoResponse mapBlogDtoResponse(BlogEntity blog){
        return BlogDtoResponse.builder()
                .blogId(blog.getBlogId())
                .name(blog.getName())
                .content(blog.getContent())
                .postedBy(blog.getPostedBy())
                .image(blog.getImage())
                .createdAt(blog.getCreatedAt())
                .blogTags(blog.getBlogTags())
                .likeCounts(blog.getLikeCounts())
                .viewCounts(blog.getViewCounts())
                .published(blog.isPublished())
                .build();
    }
}
