package pro.sky.adsplatform.constants;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class TestSecurityConstants {
    public static final String SECURITY_USER_NAME = "user@gmail.com";

    public static final String SECURITY_USER_ROLE = "USER";

    public static final String SECURITY_USER_PASSWORD = "psw";

    public static final String SECURITY_ADMIN_NAME = "admin@gmail.com";

    public static final String SECURITY_ADMIN_ROLE = "ADMIN";

    public static final String SECURITY_ADMIN_PASSWORD = "psw";

    public static final UserDetails SECURITY_USER_DETAILS =
            User.withDefaultPasswordEncoder()
            .password(SECURITY_USER_PASSWORD)
            .username(SECURITY_USER_NAME)
            .roles(SECURITY_USER_ROLE)
            .build();
}
