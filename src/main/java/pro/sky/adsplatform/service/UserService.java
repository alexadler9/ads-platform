package pro.sky.adsplatform.service;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.UserRepository;

import javax.naming.NotContextException;
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
     * @return пользователь.
     * @throws NotFoundException пользователь с указанным ID отсутствует в базе.
     */
    public UserEntity findUser(long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User not found"));
    }

    /**
     * Возвращает пользователя по указанному username.
     *
     * @param username username пользователя.
     * @return пользователь.
     * @throws NotFoundException пользователь с указанным username отсутствует в базе.
     */
    public UserEntity findUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found"));
    }

    /**
     * Возвращает пользователя по указанному username.
     *
     * @param username username пользователя.
     * @return пользователь.
     */
    @SneakyThrows
    public UserEntity findUserContentByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new NotContextException("No content for user"));
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
     * @return обновленный пользователь.k
     */
    public UserEntity updateUser(UserEntity user) {
        UserEntity userBD = findUserContentByName(user.getUsername());
        userBD.setFirstName(user.getFirstName());
        userBD.setLastName(user.getLastName());
        userBD.setPhone(user.getPhone());

        return userRepository.save(userBD);
    }
}
