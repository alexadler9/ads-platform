package pro.sky.adsplatform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @org.springframework.beans.factory.annotation.Autowired
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
            summary = "addAds",
            description = "Добавить объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    //IMAGE
//    @RequestMapping(value = "", method = RequestMethod.POST,
    //          consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AdsDto> addAds(Authentication authentication,
                                         @RequestPart("properties") CreateAdsDto createAdsDto,
                                         @RequestPart("image") MultipartFile file) throws IOException {
//Part 1
        UserEntity user = userService.findUserByName(authentication.getName());
        if (user == null) {
            return new ResponseEntity<AdsDto>(HttpStatus.NOT_FOUND);
        }

        AdsEntity ads = createAdsMapper.createAdsDtoToAds(createAdsDto);
        ads.setAuthor(user);
        ads = adsService.createAds(ads);

//Part2
        String imageNo = adsImageService.createImage(ads, file);
        AdsDto adsDto = adsMapper.adsToAdsDto(ads);
        adsDto.setImage("ads/image/" + imageNo);
        return ResponseEntity.ok(adsDto);
    }

    @Operation(
            summary = "addAdsComments",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("{ad_pk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsComments(Authentication authentication,
                                                        @PathVariable("ad_pk") String adPk,
                                                        @RequestBody AdsCommentDto adsCommentDto) {
        UserEntity user = userService.findUserByName(authentication.getName());
        if (user == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }

        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        if (ads == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }

        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto);
        adsComment.setAuthor(user);
        adsComment.setAds(ads);
        adsCommentService.createAdsComment(adsComment);

        return ResponseEntity.ok(adsCommentDto);
    }

    //IMAGE
    @GetMapping(value = "/image/{pk}")
    public ResponseEntity<byte[]> getImageNo(@PathVariable("pk") Integer pk) {
        AdsImageEntity adsImage = adsImageService.findImage(Long.valueOf(pk));
        if (adsImage == null) {
            throw new org.webjars.NotFoundException("Не найдена картинка");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(adsImage.getImage().length);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(adsImage.getImage());
    }

    @Operation(
            summary = "deleteAdsComment",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsComment(Authentication authentication,
                                                 @PathVariable("ad_pk") String adPk,
                                                 @PathVariable("id") Integer id) {
        AdsCommentEntity adsComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk));
        if (adsComment == null) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }

        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        if (ads == null) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        adsCommentService.deleteAdsComment(adsComment);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "getALLAds",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("")
    public ResponseEntity<ResponseWrapperAdsDto> getAllAds() {
        List<AdsEntity> adsList = adsService.findAllAds();
        if (adsList.size() == 0) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "getAdsComments",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsComment(@PathVariable("ad_pk") String adPk,
                                                       @PathVariable("id") Integer id) {
        AdsCommentEntity adsComment = adsCommentService.findAdsComment(id, Long.parseLong(adPk));
        if (adsComment == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsComment));
    }

    @Operation(
            summary = "getAdsComments",
            description = "!",
            tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ResponseWrapperAdsCommentDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema()))
    }
    )
    @GetMapping("{ad_pk}/comment")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAllAdsComments(@PathVariable("ad_pk") String adPk) {
        List<AdsCommentEntity> adsCommentList = adsCommentService.findAllAdsComments(Long.parseLong(adPk));
        if (adsCommentList.size() == 0) {
            return new ResponseEntity<ResponseWrapperAdsCommentDto>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseWrapperAdsCommentMapper
                .adsCommentListToResponseWrapperAdsCommentDto(adsCommentList.size(), adsCommentList));
    }

    @Operation(
            summary = "getAdsMe",
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMe(Authentication authentication,
                                                          @RequestParam(required = false) Boolean authenticated,
                                                          @RequestParam(required = false) String authorities0Authority,
                                                          @RequestParam(required = false) Object credentials,
                                                          @RequestParam(required = false) Object details,
                                                          @RequestParam(required = false) Object principal) {
        UserEntity user = userService.findUserByName(authentication.getName());
        if (user == null) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

        List<AdsEntity> adsList = adsService.findAllAdsByAuthor(user);
        if (adsList.size() == 0) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @Operation(
            summary = "getAds",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "*/*", schema = @Schema(implementation = FullAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<FullAdsDto> getAds(@PathVariable("id") Integer id) {
        AdsEntity ads = adsService.findAds(id);
        if (ads == null) {
            return new ResponseEntity<FullAdsDto>(HttpStatus.NOT_FOUND);
        }

        UserEntity user = userService.findUserByName(ads.getAuthor().getUsername());
        if (user == null) {
            return new ResponseEntity<FullAdsDto>(HttpStatus.NOT_FOUND);
        }

        FullAdsDto fullAdsDto = fullAdsMapper.adsToFullAdsDto(ads);
        fullAdsDto.setAuthorFirstName(user.getFirstName());
        fullAdsDto.setAuthorLastName(user.getLastName());
        fullAdsDto.setPhone(user.getPhone());
        fullAdsDto.setImage("ads/image/" + ads.getImages().get(0).getId().toString());

        return ResponseEntity.ok(fullAdsDto);
    }

    @Operation(
            summary = "removeAds",
            description = "!",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeAds(Authentication authentication, @PathVariable("id") Integer id) {
        AdsEntity ads = adsService.findAds(id);
        if (ads == null) {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        adsService.deleteAds(ads);

        return ResponseEntity.ok(null);
    }

    @Operation(
            summary = "updateAdsComment",
            description = "!Обновить комментарий",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsComment(Authentication authentication,
                                                          @PathVariable("ad_pk") String adPk,
                                                          @PathVariable("id") Integer id,
                                                          @RequestBody AdsCommentDto adsCommentDto) {
        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        if (ads == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);
        }

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
                !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.FORBIDDEN);
        }

        try {
            adsCommentService.updateAdsComment(adsCommentMapper.adsCommentDtoToAdsComment(adsCommentDto), id, Long.parseLong(adPk));
        } catch (IllegalArgumentException | NotFoundException e) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(adsCommentDto);
    }

    @Operation(
            summary = "updateAds",
            description = "!Редактировать объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{id}")
    public ResponseEntity<AdsDto> updateAds(Authentication authentication,
                                            @PathVariable("id") Integer id,
                                            @RequestBody AdsDto adsDto) {
        AdsEntity ads = adsService.findAds(id);
        if (ads == null) {
            return new ResponseEntity<AdsDto>(HttpStatus.NO_CONTENT);
        }

        String meName = authentication.getName();
        String authorName = ads.getAuthor().getUsername();
        if (!authService.hasRole(meName, UserEntity.UserRole.ADMIN.name()) &&
            !meName.equals(authorName)) {
            return new ResponseEntity<AdsDto>(HttpStatus.FORBIDDEN);
        }

        try {
            adsService.updateAds(adsMapper.adsDtoToAds(adsDto), id);
        } catch (IllegalArgumentException | NotFoundException e) {
            return new ResponseEntity<AdsDto>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(adsDto);
    }

    @GetMapping("search{title}")
    public ResponseEntity<ResponseWrapperAdsDto> findAllByTitleLike(@PathVariable("title") String title) {
        List<AdsEntity> adsList = adsService.findAllAdsByTitleLike(title);
        if (adsList.size() == 0) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(responseWrapperAdsMapper
                .adsListToResponseWrapperAdsDto(adsList.size(), adsList));
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{ad}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(Authentication authentication,
                                               @PathVariable Integer ad,
                                               @RequestParam MultipartFile file) throws IOException {
        AdsEntity ads = adsService.findAds(ad);
        if (ads == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        if (!authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) &&
            !authentication.getName().equals(ads.getAuthor().getUsername())) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        adsImageService.createImage(ads, file);

        return ResponseEntity.ok(null);
    }
}
