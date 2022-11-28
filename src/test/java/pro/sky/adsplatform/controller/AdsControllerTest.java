package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.repository.AdsCommentRepository;
import pro.sky.adsplatform.repository.AdsImageRepository;
import pro.sky.adsplatform.repository.AdsRepository;
import pro.sky.adsplatform.repository.UserRepository;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
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

    AdsControllerTest() {
    }

    /**
     * Тестирование работы контроллера с объявлениями.
     */

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAddAds() throws Exception {
        byte[] fileContent = new byte[] { 0x00 };
        MockPart filePart = new MockPart("image", "image.jpeg", fileContent);

        byte[] adsContent = objectMapper.writeValueAsString(CREATE_ADS_DTO).getBytes(UTF_8);
        MockPart adsPart = new MockPart("properties", "createAdsDto", adsContent);
        adsPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        mockMvc.perform(multipart("http://localhost:3000/ads")
                        .part(adsPart)
                        .part(filePart)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.title").value(ADS_DTO.getTitle()));
    }

    @Test
    void shouldReturnUnauthorizedWhenAddAds() throws Exception {
        byte[] fileContent = new byte[] { 0x00 };
        MockPart filePart = new MockPart("image", "image.jpeg", fileContent);

        byte[] adsContent = objectMapper.writeValueAsString(CREATE_ADS_DTO).getBytes(UTF_8);
        MockPart adsPart = new MockPart("properties", "createAdsDto", adsContent);
        adsPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        mockMvc.perform(multipart("http://localhost:3000/ads")
                        .part(adsPart)
                        .part(filePart)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenAddAdsForMissingUser() throws Exception {
        byte[] fileContent = new byte[] { 0x00 };
        MockPart filePart = new MockPart("image", "image.jpeg", fileContent);

        byte[] adsContent = objectMapper.writeValueAsString(CREATE_ADS_DTO).getBytes(UTF_8);
        MockPart adsPart = new MockPart("properties", "createAdsDto", adsContent);
        adsPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        mockMvc.perform(multipart("http://localhost:3000/ads")
                        .part(adsPart)
                        .part(filePart)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAuthorUpdateAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);

        mockMvc.perform(patch("http://localhost:3000/ads/{id}", id)
                        .content(objectMapper.writeValueAsString(ADS_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.title").value(ADS_DTO.getTitle()));
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminUpdateAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);

        mockMvc.perform(patch("http://localhost:3000/ads/{id}", id)
                        .content(objectMapper.writeValueAsString(ADS_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.title").value(ADS_DTO.getTitle()));
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedUpdateAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);

        mockMvc.perform(patch("http://localhost:3000/ads/{id}", id)
                        .content(objectMapper.writeValueAsString(ADS_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdateAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);

        mockMvc.perform(patch("http://localhost:3000/ads/{id}", id)
                        .content(objectMapper.writeValueAsString(ADS_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenUpdateAdsForMissingAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(adsRepository.save(any(AdsEntity.class))).thenReturn(ADS);

        mockMvc.perform(patch("http://localhost:3000/ads/{id}", id)
                        .content(objectMapper.writeValueAsString(ADS_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAllAds() throws Exception {
        final List<AdsEntity> adsList = new ArrayList<>();
        adsList.add(ADS);

        when(adsRepository.findAll()).thenReturn(adsList);

        mockMvc.perform(get("http://localhost:3000/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(adsList.size()))
                .andExpect(jsonPath("$.results[0].author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.results[0].pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.results[0].price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.results[0].title").value(ADS_DTO.getTitle()));
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenGetAllAdsForMe() throws Exception {
        final List<AdsEntity> adsList = new ArrayList<>();
        adsList.add(ADS);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findAllByAuthor(any(UserEntity.class))).thenReturn(adsList);

        mockMvc.perform(get("http://localhost:3000/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(adsList.size()))
                .andExpect(jsonPath("$.results[0].author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.results[0].pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.results[0].price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.results[0].title").value(ADS_DTO.getTitle()));
    }

    @Test
    void shouldReturnUnauthorizedWhenGetAllAdsForMe() throws Exception {
        final List<AdsEntity> adsList = new ArrayList<>();
        adsList.add(ADS);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findAllByAuthor(any(UserEntity.class))).thenReturn(adsList);

        mockMvc.perform(get("http://localhost:3000/ads/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenGetAllAdsForNotMe() throws Exception {
        final List<AdsEntity> adsList = new ArrayList<>();
        adsList.add(ADS);

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());
        when(adsRepository.findAllByAuthor(any(UserEntity.class))).thenReturn(adsList);

        mockMvc.perform(get("http://localhost:3000/ads/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));

        mockMvc.perform(get("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorFirstName").value(FULL_ADS_DTO.getAuthorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(FULL_ADS_DTO.getAuthorLastName()))
                .andExpect(jsonPath("$.description").value(FULL_ADS_DTO.getDescription()))
                .andExpect(jsonPath("$.email").value(FULL_ADS_DTO.getEmail()))
                .andExpect(jsonPath("$.image").value(FULL_ADS_DTO.getImage()))
                .andExpect(jsonPath("$.phone").value(FULL_ADS_DTO.getPhone()))
                .andExpect(jsonPath("$.pk").value(FULL_ADS_DTO.getPk()))
                .andExpect(jsonPath("$.price").value(FULL_ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.title").value(FULL_ADS_DTO.getTitle()));
    }

    @Test
    void shouldReturnNotFoundWhenGetAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAdsByTitle() throws Exception {
        final String title = "title";
        final List<AdsEntity> adsList = new ArrayList<>();
        adsList.add(ADS);

        when(adsRepository.findAllByTitleLikeIgnoreCase(any(String.class))).thenReturn(adsList);

        mockMvc.perform(get("http://localhost:3000/ads/search{title}", title))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(adsList.size()))
                .andExpect(jsonPath("$.results[0].author").value(ADS_DTO.getAuthor()))
                .andExpect(jsonPath("$.results[0].image").value(ADS_DTO.getImage()))
                .andExpect(jsonPath("$.results[0].pk").value(ADS_DTO.getPk()))
                .andExpect(jsonPath("$.results[0].price").value(ADS_DTO.getPrice()))
                .andExpect(jsonPath("$.results[0].title").value(ADS_DTO.getTitle()));
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAuthorDeleteAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        doNothing().when(adsRepository).delete(any(AdsEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminDeleteAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        doNothing().when(adsRepository).delete(any(AdsEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedDeleteAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        doNothing().when(adsRepository).delete(any(AdsEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenDeleteAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        doNothing().when(adsRepository).delete(any(AdsEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenDeleteAdsForMissingAds() throws Exception {
        final Long id = 1L;

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        doNothing().when(adsRepository).delete(any(AdsEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{id}", id))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование работы контроллера с изображениями.
     */

    @Test
    void shouldReturnOkWhenGetImage() throws Exception {
        final Long pk = 1L;

        when(adsImageRepository.findById(any(Long.class))).thenReturn(Optional.of(ADS_IMAGE));

        mockMvc.perform(get("http://localhost:3000/ads/image/{pk}", pk))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenGetImage() throws Exception {
        final Long pk = 1L;

        when(adsImageRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("http://localhost:3000/ads/image/{pk}", pk))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenUpdateImage() throws Exception {
        final Long ad = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{ad}/image", ad);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAuthorUpdateImage() throws Exception {
        final Long id = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{id}/image", id);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminUpdateImage() throws Exception {
        final Long id = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{id}/image", id);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedUpdateImage() throws Exception {
        final Long id = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{id}/image", id);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdateImage() throws Exception {
        final Long id = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{id}/image", id);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenUpdateImageForMissingAds() throws Exception {
        final Long id = 1L;
        final MockMultipartFile file = new MockMultipartFile("image", "image.jpeg", "image/jpeg", new byte[] { 0x00 });

        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        when(adsImageRepository.save(any(AdsImageEntity.class))).thenReturn(ADS_IMAGE);

        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("http://localhost:3000/ads/{id}/image", id);
        builder.with(request -> { request.setMethod("PATCH"); return request; });

        mockMvc.perform(builder.file(file))
                .andExpect(status().isNotFound());
    }

    /**
     * Тестирование работы контроллера с отзывами.
     */

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAddComment() throws Exception {
        final Long adPk = 1L;

        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(USER));
        when(adsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(ADS));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comments", adPk)
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

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comments", adPk)
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

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comments", adPk)
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

        mockMvc.perform(post("http://localhost:3000/ads/{ad_pk}/comments", adPk)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnOkWhenAuthorUpdateComment() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(patch("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id)
                .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ADS_COMMENT_DTO.getPk()))
                .andExpect(jsonPath("$.text").value(ADS_COMMENT_DTO.getText()))
                .andExpect(jsonPath("$.author").value(ADS_COMMENT_DTO.getAuthor()))
                .andExpect(jsonPath("$.createdAt").value(ADS_COMMENT_DTO.getCreatedAt().format(dateTimeFormatter)));
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminUpdateComment() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(patch("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ADS_COMMENT_DTO.getPk()))
                .andExpect(jsonPath("$.text").value(ADS_COMMENT_DTO.getText()))
                .andExpect(jsonPath("$.author").value(ADS_COMMENT_DTO.getAuthor()))
                .andExpect(jsonPath("$.createdAt").value(ADS_COMMENT_DTO.getCreatedAt().format(dateTimeFormatter)));
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedUpdateComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(patch("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenUpdateComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(patch("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id)
                        .content(objectMapper.writeValueAsString(ADS_COMMENT_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = SECURITY_USER_NAME, password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnNotFoundWhenUpdateCommentForMissingComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        when(adsCommentRepository.save(any(AdsCommentEntity.class))).thenReturn(ADS_COMMENT);

        mockMvc.perform(patch("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id)
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

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = SECURITY_ADMIN_NAME, password = SECURITY_ADMIN_PASSWORD, roles = { SECURITY_ADMIN_ROLE, SECURITY_USER_ROLE })
    void shouldReturnOkWhenAdminDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "undefined", password = SECURITY_USER_PASSWORD, roles = SECURITY_USER_ROLE)
    void shouldReturnForbiddenWhenUndefinedDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedWhenDeleteComment() throws Exception {
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));
        doNothing().when(adsCommentRepository).delete(any(AdsCommentEntity.class));

        mockMvc.perform(delete("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnOkWhenGetComment() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final Long id = 1L;

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.of(ADS_COMMENT));

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
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

        when(adsCommentRepository.findFirstByIdAndAdsId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comments/{id}", adPk, id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnOkWhenGetAllComments() throws Exception {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        final Long adPk = 1L;
        final List<AdsCommentEntity> adsCommentList = new ArrayList<>();
        adsCommentList.add(ADS_COMMENT);

        when(adsCommentRepository.findAllByAdsId(any(Long.class))).thenReturn(adsCommentList);

        mockMvc.perform(get("http://localhost:3000/ads/{ad_pk}/comments", adPk))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(adsCommentList.size()))
                .andExpect(jsonPath("$.results[0].pk").value(ADS_COMMENT_DTO.getPk()))
                .andExpect(jsonPath("$.results[0].text").value(ADS_COMMENT_DTO.getText()))
                .andExpect(jsonPath("$.results[0].author").value(ADS_COMMENT_DTO.getAuthor()))
                .andExpect(jsonPath("$.results[0].createdAt").value(ADS_COMMENT_DTO.getCreatedAt().format(dateTimeFormatter)));
    }
}