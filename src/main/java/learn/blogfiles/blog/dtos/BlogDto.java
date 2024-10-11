package learn.blogfiles.blog.dtos;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;


@Builder
public record BlogDto(
        String name,
        String content,
        String postedBy,
        String image,
        LocalDate createdAt,
        List<String> blogTags,
        int likeCounts,
        int viewCount
) {}

