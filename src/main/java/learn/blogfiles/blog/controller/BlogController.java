package learn.blogfiles.blog.controller;

import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import learn.blogfiles.blog.handlers.NotFound404Exception;
import learn.blogfiles.blog.model.BlogEntity;
import learn.blogfiles.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    private final BlogService blogService;


    @PostMapping("/post")
    public ResponseEntity<String> create(@RequestBody BlogDto blog){
        return new ResponseEntity<>(blogService.createBlog(blog), HttpStatus.CREATED.valueOf(201));
    }

    @GetMapping("/all-posts")
    public ResponseEntity<Page<BlogDtoResponse>> getAllController(Pageable pageable){
        return new ResponseEntity<>(blogService.getAllBlogs(pageable), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/get-details/{blogId}")
    public ResponseEntity<BlogDtoResponse> getDetailsController(@PathVariable String blogId){
        return new ResponseEntity<>(blogService.getBlogDetails(blogId), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/find-name")
    public ResponseEntity<BlogDtoResponse> getByNameContentController(@RequestParam String name){
        return new ResponseEntity<>(blogService.getBlogNameContent(name), HttpStatusCode.valueOf(200));
    }


    @PutMapping("/update-blog/{blogId}")
    public ResponseEntity<BlogDtoResponse> updateController(@RequestBody BlogDto dto, @PathVariable String blogId){
        BlogDtoResponse newUpdate = blogService.updateBlog(dto, blogId);
        return new ResponseEntity<>(newUpdate, HttpStatus.OK);
    }

    @PatchMapping("/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable String postId) {
        try {
            blogService.likePost(postId);
            System.out.println(postId);
            return ResponseEntity.ok("Post liked successfully.");
        } catch (NotFound404Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}

