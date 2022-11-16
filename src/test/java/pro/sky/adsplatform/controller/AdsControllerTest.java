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
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.repository.AdsCommentRepository;
import pro.sky.adsplatform.repository.AdsImageRepository;
import pro.sky.adsplatform.repository.AdsRepository;
import pro.sky.adsplatform.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
class AdsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AdsRepository adsRepository;

    @MockBean
    AdsCommentRepository adsCommentRepository;

    @MockBean
    AdsImageRepository adsImageRepository;

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAddComment() throws Exception {
        final Long adPk = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comment", adPk)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(ADS_COMMENT_DTO.getText()));
    }

    @Test
    void shouldReturnUnauthorizedWhenAddComment() throws Exception {
        final Long adPk = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comment", adPk)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenAddCommentForMissingUser() throws Exception {
        final Long adPk = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comment", adPk)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenAddCommentForMissingAds() throws Exception {
        final Long adPk = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comment", adPk)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAuthorDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNoContentWhenDeleteCommentForMissingAds() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNoContentWhenDeleteCommentForMissingComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnOkWhenGetComment() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ADS_COMMENT_DTO.getPk()))
                .andExpect(jsonPath("$.text").value(ADS_COMMENT_DTO.getText()))
                .andExpect(jsonPath("$.author").value(ADS_COMMENT_DTO.getAuthor()))
                .andExpect(jsonPath("$.createdAt").value(ADS_COMMENT_DTO.getCreatedAt().format(dateTimeFormatter)));
    }

    @Test
    void shouldReturnNotFoundWhenGetComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAds_Id(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comment/{id}", adPk, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAllComments() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final List<AdsCommentEntity> adsCommentList = new ArrayList<>();
        adsCommentList.add(ADS_COMMENT);

        when(adsCommentRepository.findAllByAds_Id(any(Long.class))).thenReturn(adsCommentList);

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comment", adPk))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(adsCommentList.size()))
                .andExpect(jsonPath("$.results[0].pk").value(ADS_COMMENT_DTO.getPk()))
                .andExpect(jsonPath("$.results[0].text").value(ADS_COMMENT_DTO.getText()))
                .andExpect(jsonPath("$.results[0].author").value(ADS_COMMENT_DTO.getAuthor()))
                .andExpect(jsonPath("$.results[0].createdAt").value(ADS_COMMENT_DTO.getCreatedAt().format(dateTimeFormatter)));
    }

    @Test
    void shouldReturnNotFoundWhenGetAllComments() throws Exception {
        final Long adPk = 1L;
        final List<AdsCommentEntity> adsCommentList = new ArrayList<>();

        when(adsCommentRepository.findAllByAds_Id(any(Long.class))).thenReturn(adsCommentList);

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comment", adPk))
                .andExpect(status().isNotFound());
    }
}