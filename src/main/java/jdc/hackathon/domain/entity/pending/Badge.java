package jdc.hackathon.domain.entity.pending;

import jakarta.persistence.*;
import jdc.hackathon.domain.common.BaseTimeEntity;
import lombok.*;

@Entity
@Table(name = "badges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Badge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100)
    private String title;

    @Column(length = 255)
    private String message;

    @Column(name = "link_url", length = 255)
    private String linkUrl;

}
