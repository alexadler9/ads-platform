package pro.sky.adsplatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "ads_images")
@Getter
@Setter
@NoArgsConstructor
public class AdsImageEntity {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ads")
    private AdsEntity ads;

    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdsImageEntity that = (AdsImageEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AdsImageEntity{" +
                "id=" + id +
                ", ads=" + ads +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
