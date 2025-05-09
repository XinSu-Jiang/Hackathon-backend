package jdc.hackathon.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
    private Long userId;
    private Long postId;
}