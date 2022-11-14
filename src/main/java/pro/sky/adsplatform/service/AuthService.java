package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.dto.RegisterReqDto;
import pro.sky.adsplatform.dto.RoleDto;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.mapper.RegisterReqMapperImpl;
import pro.sky.adsplatform.repository.UserRepository;

@Service
public class AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final RegisterReqMapperImpl registerReqMapper;
    private final UserDetailsManager manager;

    private final PasswordEncoder encoder;

    public AuthService(UserRepository userRepository, RegisterReqMapperImpl registerReqMapper, UserDetailsManager manager) {
        this.userRepository = userRepository;
        this.registerReqMapper = registerReqMapper;
        this.manager = manager;
        this.encoder = new BCryptPasswordEncoder();
    }

    public void changePassword(String oldPassword, String newPassword) {
        String passwordNew = encoder.encode(newPassword);
        String passwordOld = oldPassword;

        manager.changePassword(passwordOld, passwordNew);
    }


    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }

        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String str = encryptedPassword.substring(0,8);
        String ecryptedPasswordWithoutEncryptionType = encryptedPassword;
        if (str.equals("{bcrypt}"))
             ecryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);

         return encoder.matches(password, ecryptedPasswordWithoutEncryptionType);
    }

    public boolean register(RegisterReqDto registerReq, RoleDto role) {
        if (manager.userExists(registerReq.getUsername())) {
            LOGGER.info("userExists");
            return false;
        }

        manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()

        );
        UserEntity userCreated = userRepository.findByUsername(registerReq.getUsername()).orElse(null);
        if (userCreated != null) {
            UserEntity userEntity = registerReqMapper.registerReqDtoToUser(registerReq);
            userEntity.setId(userCreated.getId());
            userEntity.setEnabled(userCreated.getEnabled());
            userEntity.setPassword(userCreated.getPassword());
            LOGGER.info("middle makeProcess - id {} FN {}  LN {}  getPhone: {} username {}  password {}",
                    userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getPhone(),
                    userEntity.getUsername(), userEntity.getPassword());
            userRepository.save(userEntity);
        }

        return true;
    }
}