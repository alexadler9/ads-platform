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
    @Column(name = "id_user")
    private Long id;

    @MapsId
    @OneToOne(mappedBy = "avatar", fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private UserEntity user;

    private byte[] image;

}
