package learn.blogfiles.blog.handlers;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {


    private Integer statusCode;
    private String message;
    private String error;
    private Set<String> validationErrors;
    private Map<String, String> errors;
}
