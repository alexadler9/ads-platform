package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.UserRepository;

import java.util.List;

/**
 * Сервис для работы с пользователями.
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Возвращает пользователя по указанному ID.
     *
     * @param id ID пользователя.
     * @return пользователь. Может вернуть null, если такой пользователь отсутствует.
     */
    public UserEntity getUser(long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Возвращает всех пользователей.
     *
     * @return список всех пользователей.
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновление данные пользователя.
     *
     * @return
     */
    public UserEntity updateUser(UserEntity userEntity) {
        LOGGER.info("Обновление данных пользователя");
        if (userEntity.getId() != null) {
            userRepository.findById(userEntity.getId())
                    .ifPresent(user -> {
                        user.setFirstName(userEntity.getFirstName());
                        user.setLastName(userEntity.getLastName());
                        user.setPhone(userEntity.getPhone());
                        user.setId(userEntity.getId());
                        user.setEmail(userEntity.getEmail());
                    });
        } else {
            LOGGER.error("В базе такой пользователь отстустсвует");
            throw new NotFoundException("В базе такой пользователь отстустсвует");
        }
        return userRepository.save(userEntity);
    }

    /**
     * Обновление пароля пользователя.
     *
     * @return
     */
    public void setPassword() {
        LOGGER.info("Обновление пароля пользователя");
    }
}
