package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.dto.RegisterReqDto;
import pro.sky.adsplatform.dto.RoleDto;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.RegisterReqMapper;
import pro.sky.adsplatform.mapper.RegisterReqMapperImpl;
import pro.sky.adsplatform.repository.UserRepository;

import java.util.Collection;

/**
 * Сервис для работы с авторизацией.
 */
@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final RegisterReqMapper registerReqMapper;
    private final UserRepository userRepository;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String ENCRYPTION_PREFIX = "{bcrypt}";

    public AuthService(RegisterReqMapperImpl registerReqMapper,
                       UserRepository userRepository,
                       UserDetailsManager manager) {
        this.registerReqMapper = registerReqMapper;
        this.userRepository = userRepository;
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    /**
     * Авторизует пользователя.
     *
     * @param username имя пользователя.
     * @param password пароль пользователя.
     * @return true, если пользователь успешно авторизован, иначе false.
     */
    public boolean login(String username, String password) {
        if (!manager.userExists(username)) {
            LOGGER.info("Пользователь с такими данными не зарегистрирован");
            return false;
        }

        UserDetails userDetails = manager.loadUserByUsername(username);

        String encryptedPassword = userDetails.getPassword();
        String prefix = encryptedPassword.substring(0, ENCRYPTION_PREFIX.length());
        String ecryptedPasswordWithoutEncryptionType = encryptedPassword;
        if (prefix.equals(ENCRYPTION_PREFIX)) {
            ecryptedPasswordWithoutEncryptionType = encryptedPassword.substring(ENCRYPTION_PREFIX.length());
        }

        return encoder.matches(password, ecryptedPasswordWithoutEncryptionType);
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param registerReq данные для регистрации пользователя.
     * @param role        роль пользователя.
     * @return true, если пользователь успешно зарегистрирован, иначе false.
     */
    public boolean register(RegisterReqDto registerReq, RoleDto role) {
        if (manager.userExists(registerReq.getUsername())) {
            LOGGER.info("Пользователь с такими данными уже зарегистрирован");
            return false;
        }

        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()
        );

        UserEntity userBD = userRepository.findByUsername(registerReq.getUsername()).orElseThrow(
                ()-> new NotFoundException("User not found"));

            UserEntity user = registerReqMapper.registerReqDtoToUser(registerReq);
            user.setId(userBD.getId());
            user.setEnabled(userBD.getEnabled());
            user.setPassword(userBD.getPassword());
            LOGGER.info("Registered user: {}", user);
            userRepository.save(user);

        return true;
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param oldPassword старый пароль пользователя.
     * @param newPassword новый пароль пользователя.
     * @throws NotFoundException не удалось верифицировать пользователя: неверный пароль.
     */
    public void changePassword(Authentication authentication, String oldPassword, String newPassword) {
        String username = authentication.getName();
        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        String prefix = encryptedPassword.substring(0, ENCRYPTION_PREFIX.length());
        String ecryptedPasswordWithoutEncryptionType = encryptedPassword;
        if (prefix.equals(ENCRYPTION_PREFIX)) {
            ecryptedPasswordWithoutEncryptionType = encryptedPassword.substring(ENCRYPTION_PREFIX.length());
        }
        if (!encoder.matches(oldPassword, ecryptedPasswordWithoutEncryptionType)) {
            throw new NotFoundException("Password incorrect");
        }

        manager.updateUser(User.withDefaultPasswordEncoder()
                .password(newPassword)
                .username(username)
                .roles("USER")
                .build());
    }

    /**
     * Определяет, относится ли пользователь к указанной роли.
     *
     * @param username имя пользователя.
     * @param role     роль.
     * @return true, если пользователь относится к указанной роли, иначе false.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasRole(String username, String role) {
        UserDetails userDetails;
        try {
            userDetails = manager.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(ROLE_PREFIX + role));
    }
}