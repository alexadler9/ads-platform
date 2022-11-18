package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.adsplatform.entity.UserEntity;

import pro.sky.adsplatform.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pro.sky.adsplatform.constants.TestDtoConstants.*;
import static pro.sky.adsplatform.constants.TestEntityConstants.*;
import static pro.sky.adsplatform.constants.TestSecurityConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @Test
    void shouldReturnNotFoundWhenGetAllUsers() throws Exception {
        final List<UserEntity> userList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(userList);

        mockMvc.perform(get("http://localhost:3000/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenUpdateUserData() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(USER));
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER);

        mockMvc.perform(patch("http://localhost:3000/me", id)
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
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenUpdatePassword() throws Exception {
        final Long id = 1L;
        final String oldPassword = "password";
        final String newPasswordDto = "psw";

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(USER));
        when(userRepository.save(any(UserEntity.class))).thenReturn(USER);

        mockMvc.perform(post("http://localhost:3000/users/set_password", oldPassword, newPasswordDto)
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
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenGetUserById() throws Exception {
        final Long id = 1L;

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(USER));
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:3000/users/{id}", id))
                .andExpect(status().isOk());

    }

}