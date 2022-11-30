package pro.sky.adsplatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ads_comments")
@Getter
@Setter
@NoArgsConstructor
public class AdsCommentEntity {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "id_ads")
    private AdsEntity ads;

    @Column(name = "comment_time")
    private LocalDateTime dateTime;

    @Column(name = "comment_text")
    @NotNull
    private String text;

}
