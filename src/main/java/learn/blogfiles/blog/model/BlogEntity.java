package learn.blogfiles.blog.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.List;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String blogId;

    private String name;
    private String content;
    private String postedBy;  // by user
    private String image;  // user profile image
    private LocalDate createdAt;
    private int likeCounts;
    private int viewCounts;
    @ElementCollection
    private List<String> blogTags;
    private boolean published;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments;


}

