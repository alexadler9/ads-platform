package pro.sky.adsplatform.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ads_comments")
@DynamicInsert
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
    private UserEntity ads;

    @Column(name = "comment_time")
    private LocalDateTime dateTime;

    @Column(name = "comment_text")
    private String text;

    public AdsCommentEntity() {
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

    public UserEntity getAds() {
        return ads;
    }

    public void setAds(UserEntity ads) {
        this.ads = ads;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdsCommentEntity that = (AdsCommentEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return "AdsCommentEntity{" +
                "id=" + id +
                ", author=" + author +
                ", ads=" + ads +
                ", dateTime=" + dateTime +
                ", text='" + text + '\'' +
                '}';
    }
}
