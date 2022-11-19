package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.adsplatform.dto.NewPasswordDto;
import pro.sky.adsplatform.entity.UserEntity;

import pro.sky.adsplatform.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.adsplatform.constants.TestDtoConstants.*;
import static pro.sky.adsplatform.constants.TestEntityConstants.*;
import static pro.sky.adsplatform.constants.TestSecurityConstants.*;
import static pro.sky.adsplatform.constants.TestSecurityConstants.SECURITY_ADMIN_ROLE;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserDetailsManager manager;

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenGetAllUsers() throws Exception {
        final List<UserEntity> userList = new ArrayList<>();
        userList.add(USER);

        when(userRepository.findAll()).thenReturn(userList);

        mockMvc.perform(get("http://localhost:3000/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(userList.size()))
                .andExpect(jsonPath("$.results[0].firstName").value(USER_DTO.getFirstName()))
                .andExpect(jsonPath("$.results[0].lastName").value(USER_DTO.getLastName()))
                .andExpect(jsonPath("$.results[0].id").value(USER_DTO.getId()))
                .andExpect(jsonPath("$.results[0].email").value(USER_DTO.getEmail()))
                .andExpect(jsonPath("$.results[0].phone").value(USER_DTO.getPhone()));
    }

    @Test
    void shouldReturnUnauthorizedWhenGetAllUsers() throws Exception {
        final List<UserEntity> userList = new ArrayList<>();
        userList.add(USER);

        when(userRepository.findAll()).thenReturn(userList);

        mockMvc.perform(get("http://localhost:3000/users/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenUpdateUserData() throws Exception {
        final Long id = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER);

        mockMvc.perform(patch("http://localhost:3000/users/me", id)
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(USER_DTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(USER_DTO.getLastName()))
                .andExpect(jsonPath("$.id").value(USER_DTO.getId()))
                .andExpect(jsonPath("$.email").value(USER_DTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(USER_DTO.getPhone()));
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdateUserData() throws Exception {
        final Long id = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER);

        mockMvc.perform(patch("http://localhost:3000/users/me", id)
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNoContentWhenUpdateUserData() throws Exception {
        final Long id = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER);

        mockMvc.perform(patch("http://localhost:3000/users/me", id)
                        .content(objectMapper.writeValueAsString(USER_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenUpdatePassword() throws Exception {
        when(manager.loadUserByUsername(any(String.class))).thenReturn(SECURITY_USER_DETAILS);
        doNothing().when(manager).updateUser(any(UserDetails.class));

        mockMvc.perform(post("http://localhost:3000/users/set_password")
                        .content(objectMapper.writeValueAsString(NEW_PASSWORD_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPassword").value(NEW_PASSWORD_DTO.getCurrentPassword()))
                .andExpect(jsonPath("$.newPassword").value(NEW_PASSWORD_DTO.getNewPassword()));
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdatePassword() throws Exception {
        when(manager.loadUserByUsername(any(String.class))).thenReturn(SECURITY_USER_DETAILS);
        doNothing().when(manager).updateUser(any(UserDetails.class));

        mockMvc.perform(post("http://localhost:3000/users/set_password")
                        .content(objectMapper.writeValueAsString(NEW_PASSWORD_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUpdatePasswordWithIncorrectPassword() throws Exception {
        final NewPasswordDto incorrectNewPasswordDto = new NewPasswordDto();
        incorrectNewPasswordDto.setCurrentPassword(SECURITY_USER_PASSWORD + "-incorrect");
        incorrectNewPasswordDto.setNewPassword(NEW_PASSWORD_DTO.getNewPassword());

        when(manager.loadUserByUsername(any(String.class))).thenReturn(SECURITY_USER_DETAILS);
        doNothing().when(manager).updateUser(any(UserDetails.class));

        mockMvc.perform(post("http://localhost:3000/users/set_password")
                        .content(objectMapper.writeValueAsString(incorrectNewPasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenGetUserById() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:3000/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(USER_DTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(USER_DTO.getLastName()))
                .andExpect(jsonPath("$.id").value(USER_DTO.getId()))
                .andExpect(jsonPath("$.email").value(USER_DTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(USER_DTO.getPhone()));
    }

    @Test
    void shouldReturnUnauthorizedWhenGetUserById() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:3000/users/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenGetUserById() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:3000/users/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = SECURITY_ADMIN_ROLE)
    void shouldReturnForbiddenWhenGetUserById() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:3000/users/{id}", id))
                .andExpect(status().isForbidden());
    }
}