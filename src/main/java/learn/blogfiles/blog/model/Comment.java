package learn.blogfiles.blog.model;


import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String commentId;

    private String commentText;
    @Nonnull
    private LocalDate createdAt;
    private String postedBy;  // by the user.


    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private BlogEntity blog;
}
