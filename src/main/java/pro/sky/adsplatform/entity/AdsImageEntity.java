package pro.sky.adsplatform.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "ads_images")
@DynamicInsert
public class AdsImageEntity {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ads")
    private UserEntity ads;

    private byte[] image;

    public AdsImageEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getAds() {
        return ads;
    }

    public void setAds(UserEntity ads) {
        this.ads = ads;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

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
