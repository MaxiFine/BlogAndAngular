package learn.blogfiles.blog.controller;

import learn.blogfiles.blog.dtos.CommentDto;
import learn.blogfiles.blog.dtos.CommentResponse;
import learn.blogfiles.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;


    @PostMapping("{blogId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable String blogId, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.createComment(blogId, commentDto));
    }

}
