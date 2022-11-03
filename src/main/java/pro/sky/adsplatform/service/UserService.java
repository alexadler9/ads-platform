package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.UserEntity;
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
     * Обновить данные пользователя.
     *
     * @param user новые данные пользователя.
     */
    public void updateUser(UserEntity user) {
        throw new UnsupportedOperationException("user update is not supported");
    }

    /**
     * Обновить пароль пользователя.
     *
     * @param currentPassword текущий пароль пользователя.
     * @param newPassword новый пароль пользователя.
     */
    public void updateUserPassword(String currentPassword, String newPassword) {
        throw new UnsupportedOperationException("user password update is not supported");
    }
}
