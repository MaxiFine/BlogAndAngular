package learn.blogfiles.blog.controller;

import learn.blogfiles.blog.dtos.BlogDto;
import learn.blogfiles.blog.dtos.BlogDtoResponse;
import learn.blogfiles.blog.handlers.NotFound404Exception;
import learn.blogfiles.blog.service.BlogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(blogService.createBlog(blog), HttpStatus.CREATED);
    }

    @GetMapping("/all-posts")
    public ResponseEntity<Page<BlogDtoResponse>> getAllController(Pageable pageable){
        return new ResponseEntity<>(blogService.getAllBlogs(pageable), HttpStatus.OK);
    }

    @GetMapping("/get-details/{blogId}")
    public ResponseEntity<BlogDtoResponse> getDetailsController(@PathVariable String blogId){
        return new ResponseEntity<>(blogService.getBlogDetails(blogId), HttpStatus.OK);
    }

    @GetMapping("/find-name")
    public ResponseEntity<BlogDtoResponse> getByNameContentController(@RequestParam String name){
        return new ResponseEntity<>(blogService.getBlogNameContent(name), HttpStatus.OK);
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

//    @GetMapping("/test")
//    public ResponseEntity<String> returnString() {
//        String test = "Hello, this is a test response! for services";
//        return ResponseEntity.status(HttpStatus.OK).body(test);
//    }
}

