package pro.sky.adsplatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "avatars")
@Getter
@Setter
@NoArgsConstructor
public class AvatarEntity {
    @Id
    @Column(name="id_user")
    private Long id;

    @MapsId
    @OneToOne(mappedBy = "avatar", fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private UserEntity user;

    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvatarEntity that = (AvatarEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AvatarEntity{" +
                "id=" + id +
                ", user=" + user +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
