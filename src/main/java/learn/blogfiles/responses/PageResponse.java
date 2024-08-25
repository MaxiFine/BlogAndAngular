package learn.blogfiles.responses;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class PageResponse <T>{

    private List<T> content;
    private HttpStatus status;
    private String message;


    public PageResponse(List<T> content, HttpStatus status, String message) {
        this.content = content;
        this.status = status;
        this.message = message;
    }

    public PageResponse(List<T> content) {
        this.content = content;
    }

    public PageResponse(HttpStatus status) {
        this.status = status;
    }

    public PageResponse(String message) {
        this.message = message;
    }
}

