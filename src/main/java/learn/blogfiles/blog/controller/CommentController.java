package learn.blogfiles.blog.controller;

import learn.blogfiles.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @GetMapping
    public ResponseEntity<?> createComment(
            @RequestParam String blogId,
            @RequestParam String postedBy,
            @RequestParam String content) {
        return ResponseEntity.ok(commentService.createComment(blogId, postedBy, content));
    }
}
