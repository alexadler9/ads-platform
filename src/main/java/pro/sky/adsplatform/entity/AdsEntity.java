package pro.sky.adsplatform.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ads")
@DynamicInsert
public class AdsEntity {
    @Id
    @Column(columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_author")
    private UserEntity author;

    private String title;

    private String description;

    private BigDecimal price;

    @OneToMany(mappedBy = "ads", fetch = FetchType.EAGER)
    private List<AdsImageEntity> images;

    public AdsEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<AdsImageEntity> getImages() {
        return images;
    }

    public void setImages(List<AdsImageEntity> images) {
        this.images = images;
    }

    public AdsImageEntity getLastImage() {
        return (images.size() == 0) ? null : images.get(images.size() - 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdsEntity adsEntity = (AdsEntity) o;
        return id.equals(adsEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AdsEntity{" +
                "id=" + id +
                ", author=" + author +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", images=" + images +
                '}';
    }
}
