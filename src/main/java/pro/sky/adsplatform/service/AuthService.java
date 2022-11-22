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
import pro.sky.adsplatform.exception.BadRequestException;
import pro.sky.adsplatform.exception.ForbiddenException;
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
     * @throws ForbiddenException пользователь не зарегистрирован.
     * @throws ForbiddenException не удалось авторизовать пользователя: неверный пароль.
     */
    public void login(String username, String password) {
        if (!manager.userExists(username)) {
            LOGGER.error("Пользователь с такими данными не зарегистрирован");
            throw new ForbiddenException("User is not registered");
        }

        UserDetails userDetails = manager.loadUserByUsername(username);
        String encryptedPassword = userDetails.getPassword();
        String prefix = encryptedPassword.substring(0, ENCRYPTION_PREFIX.length());
        String ecryptedPasswordWithoutEncryptionType = encryptedPassword;
        if (prefix.equals(ENCRYPTION_PREFIX)) {
            ecryptedPasswordWithoutEncryptionType = encryptedPassword.substring(ENCRYPTION_PREFIX.length());
        }
        if (!encoder.matches(password, ecryptedPasswordWithoutEncryptionType)) {
            LOGGER.error("Не удалось авторизовать пользователя: неверный пароль");
            throw new ForbiddenException("User authorization failed: invalid password");
        }
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param registerReq данные для регистрации пользователя.
     * @param role роль пользователя.
     * @throws BadRequestException пользователь уже зарегистрирован.
     * @throws BadRequestException пользователь отсутствует в базе данных после регистрации.
     */
    public void register(RegisterReqDto registerReq, RoleDto role) {
        if (manager.userExists(registerReq.getUsername())) {
            LOGGER.error("Пользователь с такими данными уже зарегистрирован");
            throw new BadRequestException("User is already registered");
        }

        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()
        );

        UserEntity userBD = userRepository.findByUsername(registerReq.getUsername()).orElseThrow(
                ()-> new BadRequestException("User not found"));

        UserEntity user = registerReqMapper.registerReqDtoToUser(registerReq);
        user.setId(userBD.getId());
        user.setEnabled(userBD.getEnabled());
        user.setPassword(userBD.getPassword());
        userRepository.save(user);
    }

    /**
     * Обновляет пароль пользователя.
     *
     * @param oldPassword старый пароль пользователя.
     * @param newPassword новый пароль пользователя.
     * @throws ForbiddenException не удалось верифицировать пользователя: неверный пароль.
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
            LOGGER.error("Не удалось верифицировать пользователя: неверный пароль");
            throw new ForbiddenException("User verification failed: invalid password");
        }

        manager.updateUser(User.withDefaultPasswordEncoder()
                .password(newPassword)
                .username(username)
                .authorities(authentication.getAuthorities())
                .build());
    }

    /**
     * Определяет, относится ли пользователь к указанной роли.
     *
     * @param username имя пользователя.
     * @param role роль.
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