package jdc.hackathon.domain.dto.post;

import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import jdc.hackathon.domain.enumType.PostStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSummaryResponse {
    private Long id;
    private String title;
    private District location;
    private PostCategory category;
    private PostStatus status;
    private LocalDateTime createdAt;
}