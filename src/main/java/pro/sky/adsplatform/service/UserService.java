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
    public UserEntity findUser(long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Возвращает пользователя по указанному username.
     *
     * @param username username пользователя.
     * @return пользователь. Может вернуть null, если такой пользователь отсутствует.
     */
    public UserEntity findUserByName(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Возвращает всех пользователей.
     *
     * @return список всех пользователей.
     */
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновляет данные пользователя (поля firstName, lastName, phone).
     *
     * @param user обновленные данные пользователя.
     * @throws NotFoundException пользователь с указанными параметрами отсутствует в базе.
     */
    public void updateUser(UserEntity user) {
        UserEntity userBD = findUserByName(user.getUsername());
        if (userBD == null) {
            LOGGER.error("Пользователь с таким Именем отсутствует");
            throw new NotFoundException("Пользователь с таким именем отсутствует");
        }

        if (user.getFirstName() != null) {
            userBD.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            userBD.setLastName(user.getLastName());
        }
        if (user.getPhone() != null) {
            userBD.setPhone(user.getPhone());
        }

        userRepository.save(userBD);
    }
}
