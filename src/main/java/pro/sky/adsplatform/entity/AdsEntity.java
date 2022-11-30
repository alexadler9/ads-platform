package pro.sky.adsplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ads")
@Getter
@Setter
@NoArgsConstructor
public class AdsEntity {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private UserEntity author;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal price;

    @JsonIgnore
    @OneToMany(mappedBy = "ads", fetch = FetchType.EAGER)
    private List<AdsImageEntity> images;

    public AdsImageEntity getLastImage() {
        return ((images == null) || (images.size()) == 0) ? null : images.get(images.size() - 1);
    }

}
