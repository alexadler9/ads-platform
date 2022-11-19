package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.*;
import pro.sky.adsplatform.service.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsController.class);

    private final AdsMapper adsMapper;
    private final FullAdsMapper fullAdsMapper;
    private final CreateAdsMapper createAdsMapper;
    private final AdsCommentMapper adsCommentMapper;
    private final ResponseWrapperAdsMapper responseWrapperAdsMapper;
    private final ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper;
    private final UserService userService;
    private final AdsService adsService;
    private final AdsCommentService adsCommentService;
    private final AdsImageService adsImageService;
    private final AuthService authService;

    @Autowired
    public AdsController(AdsMapper adsMapper,
                         FullAdsMapper fullAdsMapper,
                         AdsCommentMapper adsCommentMapper,
                         CreateAdsMapper createAdsMapper,
                         ResponseWrapperAdsMapper responseWrapperAdsMapper,
                         ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper,
                         UserService userService,
                         AdsService adsService,
                         AdsCommentService adsCommentService,
                         AdsImageService adsImageService,
                         AuthService authService) {
        this.adsMapper = adsMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.adsCommentMapper = adsCommentMapper;
        this.createAdsMapper = createAdsMapper;
        this.responseWrapperAdsMapper = responseWrapperAdsMapper;
        this.responseWrapperAdsCommentMapper = responseWrapperAdsCommentMapper;
        this.userService = userService;
        this.adsService = adsService;
        this.adsCommentService = adsCommentService;
        this.adsImageService = adsImageService;
        this.authService = authService;
    }

    @Operation(
            summary = "Добавить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно добавлено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Автор не найден")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AdsDto> addAds(
            Authentication authentication,
            @Parameter(description = "Параметры объявления")
            @RequestPart("properties") CreateAdsDto createAdsDto,
            @Parameter(description = "Изображение")
            @RequestPart("image") MultipartFile file
    ) throws IOException {
        UserEntity user = userService.findUserByName(authentication.getName());

        AdsEntity ads = createAdsMapper.createAdsDtoToAds(createAdsDto);
        ads.setAuthor(user);
        AdsEntity adsCreated = adsService.createAds(ads);

        String imageId = adsImageService.createImage(adsCreated, file);
        AdsDto adsDto = adsMapper.adsToAdsDto(adsCreated);
        adsDto.setImage("ads/image/" + imageId);
        return ResponseEntity.ok(adsDto);
    }

    @Operation(
            summary = "Добавить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно добавлен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Автор и/или объявление не найдены")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("{ad_pk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsComments(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры отзыва")
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        UserEntity user = userService.findUserByName(authentication.getName());


        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));

        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto);
        adsComment.setAuthor(user);
        adsComment.setAds(ads);
        AdsCommentEntity adsCommentCreated = adsCommentService.createAdsComment(adsComment);

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentCreated));
    }

    @Operation(
            summary = "Получить изображение по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно получено"),
                    @ApiResponse(responseCode = "404", description = "Изображение не найдено")
            }
    )
    @GetMapping(value = "/image/{pk}")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "ID изображения")
            @PathVariable("pk") Integer pk
    ) {
        AdsImageEntity adsImage = adsImageService.findImage(Long.valueOf(pk));


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(adsImage.getImage().length);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(adsImage.getImage());
    }

    @Operation(
            summary = "Удалить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно удален"),
                    @ApiResponse(responseCode = "204", description = "Отзыв и/или объявление не найдены"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsComment(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id
    ) {
        AdsCommentEntity adsComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk));

        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        if (ads == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsCommentService.deleteAdsComment(adsComment);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "Получить список всех объявлений",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Список пуст")
            }
    )
    @GetMapping("")
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        List<AdsEntity> adsList = adsService.findAllAds();
//        if (adsList.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Получить отзыв по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден")
            }
    )
    @GetMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id
    ) {
        AdsCommentEntity adsComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk));

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsComment));
    }

    @Operation(
            summary = "Получить список отзывов",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Список пуст")
            }
    )
    @GetMapping("{ad_pk}/comment")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllAdsComments(
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk
    ) {
        List<AdsCommentEntity> adsCommentList = adsCommentService.findAllAdsComments(Long.parseLong(adPk));
//        if (adsCommentList.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
        return ResponseEntity.ok(responseWrapperAdsCommentMapper
                .adsCommentListToResponseWrapperAdsCommentDto(adsCommentList.size(), adsCommentList));
    }

    @Operation(
            summary = "Получить список объявлений пользователя",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Список пуст")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(
            Authentication authentication,
            @RequestParam(required = false) Boolean authenticated,
            @RequestParam(required = false) String authorities0Authority,
            @RequestParam(required = false) Object credentials,
            @RequestParam(required = false) Object details,
            @RequestParam(required = false) Object principal
    ) {
        UserEntity user = userService.findUserByName(authentication.getName());

        List<AdsEntity> adsList = adsService.findAllAdsByAuthor(user);
//        if (adsList.size() == 0) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Получить объявление по ID",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно получено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
                    @ApiResponse(responseCode = "404", description = "Объявление не найдено")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<FullAdsDto> getAds(
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id
    ) {
        AdsEntity ads = adsService.findAds(id);

        return ResponseEntity.ok(fullAdsMapper.adsToFullAdsDto(ads));
    }

    @Operation(
            summary = "Удалить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно удалено"),
                    @ApiResponse(responseCode = "204", description = "Объявление не найдено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeAds(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id
    ) {
        AdsEntity ads = adsService.findAdsContent(id);

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsService.deleteAds(ads);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "Обновить отзыв",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлен"),
                    @ApiResponse(responseCode = "204", description = "Объявление и/или отзыв не найдены"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("ad_pk") String adPk,
            @Parameter(description = "ID отзыва")
            @PathVariable("id") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры отзыва")
            @RequestBody AdsCommentDto adsCommentDto
    ) {
        AdsEntity ads = adsService.findAdsContent(Long.parseLong(adPk));

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AdsCommentEntity adsCommentUpdated;
        try {
            adsCommentUpdated = adsCommentService.updateAdsComment(adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto), id, Long.parseLong(adPk));
        } catch (IllegalArgumentException | NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentUpdated));
    }

    @Operation(
            summary = "Обновить объявление",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Объявление успешно обновлено"),
                    @ApiResponse(responseCode = "204", description = "Объявление не найдено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{id}")
    public ResponseEntity<AdsDto> updateAds(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable("id") Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Параметры объявления")
            @RequestBody AdsDto adsDto
    )  {
        AdsEntity ads = adsService.findAdsContent(id);

        String meName = authentication.getName();
        String authorName = ads.getAuthor().getUsername();
        if (!authService.hasRole(meName, UserEntity.UserRole.ADMIN.name()) &&
            !meName.equals(authorName)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        AdsEntity adsUpdated;

        try {
            adsUpdated = adsService.updateAds(adsMapper.adsDtoToAds(adsDto), id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(adsMapper.adsToAdsDto(adsUpdated));
    }

    @Operation(
            summary = "Получить список объявлений, совпадающих с шаблоном",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список успешно получен"),
                    @ApiResponse(responseCode = "404", description = "Список пуст")
            }
    )
    @GetMapping("search{title}")
    public ResponseEntity<ResponseWrapperAdsDto> findAllAdsByTitleLike(
            @Parameter(description = "Шаблон")
            @PathVariable("title") String title
    ) {
        List<AdsEntity> adsList = adsService.findAllAdsByTitleLike(title);

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "Добавить изображение",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Изображение успешно добавлено"),
                    @ApiResponse(responseCode = "204", description = "Объявление не найдено"),
                    @ApiResponse(responseCode = "401", description = "Требуется авторизация"),
                    @ApiResponse(responseCode = "403", description = "Доступ запрещен")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{ad}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(
            Authentication authentication,
            @Parameter(description = "ID объявления")
            @PathVariable Integer ad,
            @Parameter(description = "Изображение")
            @RequestParam MultipartFile file
    ) throws IOException {
        AdsEntity ads = adsService.findAdsContent(ad);

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
            !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        adsImageService.createImage(ads, file);

        return ResponseEntity.ok(null);
    }
}
