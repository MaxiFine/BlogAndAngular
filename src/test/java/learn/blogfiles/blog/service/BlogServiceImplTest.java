package learn.blogfiles.blog.service;

import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.mappers.BlogMapper;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.blog.repository.BlogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlogServiceImplTest {

    @InjectMocks
    private BlogServiceImpl blogServiceImpl;

    @InjectMocks
    private BlogService blogService;

    @Mock
    private BlogMapper blogMapper;
    @Mock
    private BlogDto blogDto;

    @Mock
    private BlogRepository blogRepository;


  /**  @Test
    void createBlog() {

        // Arrange
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setBlogId(String.valueOf(1L));

        when(blogMapper.mapToBlogEntity(blogDto)).thenReturn(blogEntity);

        // Act
        String result = blogService.createBlog(blogDto);

        // Assert
        verify(blogRepository).save(blogEntity);
        assertEquals("Blog created Successfully... id is: 1", result);

    }
  */
}