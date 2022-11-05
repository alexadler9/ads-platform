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

    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    public boolean register(RegisterReqDto registerReq, RoleDto role) {
        if (manager.userExists(registerReq.getUsername())) {
            LOGGER.info("userExists");
            return false;
        }
 /*       manager.createUser(
                User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()
        );
*/
        UserEntity userEntity = new UserEntity();
        userEntity = registerReqMapper.registerReqDtoToUser(registerReq);
        LOGGER.info("middle makeProcess - id {} FN {}  LN {}  getPhone: {} email {}  password {}  rol {}",
                userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(), userEntity.getPhone(),
                userEntity.getEmail(), userEntity.getPassword(), userEntity.getRole());
        userRepository.save(registerReqMapper.registerReqDtoToUser(registerReq));
        return true;
    }
}