package jdc.hackathon.domain.dto.post;


import jakarta.validation.constraints.Size;
import jdc.hackathon.domain.enumType.District;
import jdc.hackathon.domain.enumType.PostCategory;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequest {

    private PostCategory category;
    @Size(max = 100)
    private String title;
    private String description;
    private District location;
    private LocalDateTime donationDate;
    private Integer capacity;
    private Boolean isDonationOpen;
    private Integer maxAmount;
}