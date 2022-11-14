package pro.sky.adsplatform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {


    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login", "/register"
    };


    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user@gmail.com")
                .password("password")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin@gmail.com")
                .password("password")
                .roles("ADMIN", "USER")
                .build();
//
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        if (users.userExists(user.getUsername())) {
            users.deleteUser(user.getUsername());
        }
        if (users.userExists(admin.getUsername())) {
            users.deleteUser(admin.getUsername());
        }

        users.createUser(user);
        users.createUser(admin);
        return users;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authz) ->
                        authz
                                .mvcMatchers(AUTH_WHITELIST).permitAll()
                                .mvcMatchers("/ads/**", "/users/**").authenticated()
                                .mvcMatchers("/profile/**", "/users/**").hasRole("ADMIN")
                                .mvcMatchers("/ads/**").hasRole("USER")

                )
                .cors().disable()
                .httpBasic(withDefaults());
        return http.build();
    }
}

