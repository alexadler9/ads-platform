package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
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
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final AdsService adsService;
    private final AdsCommentService adsCommentService;
    private final AdsImageService adsImageService;
    private final AuthService authService;
    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AdsController(AdsMapper adsMapper,
                         FullAdsMapper fullAdsMapper,
                         AdsCommentMapper adsCommentMapper,
                         CreateAdsMapper createAdsMapper,
                         ResponseWrapperAdsMapper responseWrapperAdsMapper,
                         ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper,
                         ObjectMapper objectMapper,
                         UserService userService, AdsService adsService,
                         AdsCommentService adsCommentService,
                         AdsImageService adsImageService,
                         AuthService authService, HttpServletRequest request) {
        this.adsMapper = adsMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.adsCommentMapper = adsCommentMapper;
        this.createAdsMapper = createAdsMapper;
        this.responseWrapperAdsMapper = responseWrapperAdsMapper;
        this.responseWrapperAdsCommentMapper = responseWrapperAdsCommentMapper;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.adsService = adsService;
        this.adsCommentService = adsCommentService;
        this.adsImageService = adsImageService;
        this.authService = authService;
        this.request = request;
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
    public ResponseEntity<AdsDto> addAdsUsingPOST(Authentication authentication,
                                                  @RequestPart("properties") CreateAdsDto body,
                                                  @RequestPart("image") MultipartFile file) throws IOException {


//Part 1
        AdsEntity adsEntity = createAdsMapper.createAdsDtoToAds(body);
        if (adsEntity == null) {
            return new ResponseEntity<AdsDto>(HttpStatus.NOT_FOUND);
        }

        String userName = authentication.getName();
        UserEntity userEntity = userService.getUserByName(userName);
        if (userEntity == null) {
            return new ResponseEntity<AdsDto>(HttpStatus.NOT_FOUND);
        }

        adsEntity.setAuthor(userEntity);
        adsService.saveAddAds(adsEntity);

//Part2
        String imageNo = adsImageService.saveAddFile(adsEntity, file);
        AdsDto adsDto = adsMapper.adsToAdsDto(adsEntity);
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
    public ResponseEntity<AdsCommentDto> addAdsCommentsUsingPOST(Authentication authentication,
                                                                 @PathVariable("ad_pk") String adPk, @RequestBody AdsCommentDto body) {
        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(body);
        UserEntity userEntity = userService.getUserByName(authentication.getName());
        if (userEntity == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }
        AdsEntity adsEntity = adsService.findAds(Long.parseLong(adPk));
        if (adsEntity == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }

        adsComment.setAuthor(userEntity);
        adsComment.setAds(adsEntity);

        adsCommentService.createAdsComment(adsComment);
        return ResponseEntity.ok(body);

    }

    //IMAGE
    @GetMapping(value = "/image/{pk}")
    public ResponseEntity<byte[]> getImageNo(@PathVariable String pk) throws IOException {

        AdsImageEntity adsImageEntity = adsImageService.getImageEntity(Long.valueOf(pk));
        if (adsImageEntity == null) throw new org.webjars.NotFoundException("Не найдена картинка");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("image/jpeg"));
        httpHeaders.setContentLength(adsImageEntity.getImage().length);

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body(adsImageEntity.getImage());
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
    public ResponseEntity<Void> deleteAdsCommentUsingDELETE(Authentication authentication, @PathVariable("ad_pk") String adPk, @PathVariable("id") Integer id) {
        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));

        if (adsCommentEntity != null) {
            AdsEntity adsEntity = adsService.findAds(Long.parseLong(adPk));
            if (adsEntity == null) return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

            if (authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) == false)
                if (authentication.getName().equals(adsEntity.getAuthor().getUsername()) == false)
                    return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

            adsCommentService.deleteAdsComment(adsCommentEntity);
            return ResponseEntity.ok(null);

        } else {
            throw new NotFoundException("Комментарии отсутствуют");
        }
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
    public ResponseEntity<ResponseWrapperAdsDto> getALLAdsUsingGET() {

        List<AdsEntity> adsEntitys = adsService.getAllAds();
        Integer count = adsEntitys.size();
        if (count > 0) {
            ResponseWrapperAdsDto responseWrapperAdsDto = responseWrapperAdsMapper
                    .adsListToResponseWrapperAdsDto(count, adsEntitys);
            return ResponseEntity.ok(responseWrapperAdsDto);
        } else {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity<AdsCommentDto> getAdsCommentUsingGET(@PathVariable("ad_pk") String adPk, @PathVariable("id") Integer id) {
        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));

        if (adsCommentEntity != null) {
            return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentEntity));
        } else {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAdsCommentsUsingGET(@PathVariable("ad_pk") String adPk) {
        List<AdsCommentEntity> adsCommentEntity = adsCommentService.getAllAdsComments(Long.parseLong(adPk));

        Integer count = adsCommentEntity.size();
        if (count > 0) {
            return ResponseEntity.ok(responseWrapperAdsCommentMapper.adsCommentListToResponseWrapperAdsCommentDto(count, adsCommentEntity));
        } else {
            return new ResponseEntity<ResponseWrapperAdsCommentDto>(HttpStatus.NOT_FOUND);
        }

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
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMeUsingGET(Authentication authentication, @RequestParam(required = false) Boolean authenticated,
                                                                  @RequestParam(required = false) String authorities0Authority,
                                                                  @RequestParam(required = false) Object credentials,
                                                                  @RequestParam(required = false) Object details,
                                                                  @RequestParam(required = false) Object principal) {


        UserEntity userEntity = userService.getUserByName(authentication.getName());
        if (userEntity == null) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

        List<AdsEntity> adsEntitys = adsService.findAllByAuthor(userEntity);

        Integer count = adsEntitys.size();
        if(count == 0) {
            return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_FOUND);
        }

            ResponseWrapperAdsDto responseWrapperAdsDto = responseWrapperAdsMapper
                    .adsListToResponseWrapperAdsDto(count, adsEntitys);
            return ResponseEntity.ok(responseWrapperAdsDto);

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
    public ResponseEntity<FullAdsDto> getAdsUsingGET(@PathVariable("id") Integer id) {

        AdsEntity adsEntity = adsService.findAds(id);
        if (adsEntity != null) {
            UserEntity userEntity = userService.getUserByName(adsEntity.getAuthor().getUsername());
            if (userEntity == null) {
                return new ResponseEntity<FullAdsDto>(HttpStatus.NOT_FOUND);
            }

            FullAdsDto fullAdsDto = fullAdsMapper.adsToFullAdsDto(adsEntity);
            fullAdsDto.setAuthorFirstName(userEntity.getFirstName());
            fullAdsDto.setAuthorLastName(userEntity.getLastName());
            fullAdsDto.setPhone(userEntity.getPhone());
            fullAdsDto.setImage("ads/image/" + adsEntity.getImages().get(0).getId().toString());

            return ResponseEntity.ok(fullAdsDto);
        } else {
            return new ResponseEntity<FullAdsDto>(HttpStatus.NOT_FOUND);
        }
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
    public ResponseEntity<Void> removeAdsUsingDELETE(Authentication authentication, @PathVariable("id") Integer id) {

        AdsEntity adsEntity = adsService.findAds(id);
        if (adsEntity != null) {
            if (authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) == false)
                if (authentication.getName().equals(adsEntity.getAuthor().getUsername()) == false)
                    return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

            adsService.removeAdsUsingDELETE(adsEntity);
            return ResponseEntity.ok(null);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
        }
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
    //@PutMapping("/ads/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsCommentUsingPATCH(Authentication authentication,
                                                                    @PathVariable("ad_pk") String adPk, @PathVariable("id") Integer id, @RequestBody AdsCommentDto body) {
        try {
            AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(body);
            AdsEntity adsEntity = adsService.findAds(Long.parseLong(adPk));
            if (adsEntity == null) return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);

            if (authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) == false)
                if (authentication.getName().equals(adsEntity.getAuthor().getUsername()) == false)
                    return new ResponseEntity<AdsCommentDto>(HttpStatus.FORBIDDEN);

            adsCommentService.updateAdsCommentText(adsComment, id, Long.parseLong(adPk));
        } catch (IllegalArgumentException | NotFoundException e) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(body);

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
    public ResponseEntity<AdsDto> updateAdsUsingPATCH(Authentication authentication, @PathVariable("id") Integer id, @RequestBody AdsDto body) {
        String meName = authentication.getName();
        UserEntity meUser = userService.getUserByName(meName);

        AdsEntity adsEntity = adsService.findAds(id);
        AdsEntity adsEntity1 = adsMapper.adsDtoToAds(body);
        if (adsEntity == null) return new ResponseEntity<AdsDto>(HttpStatus.NO_CONTENT);
        String adsName = adsEntity.getAuthor().getUsername();

        if (authService.hasRole(meName, UserEntity.UserRole.ADMIN.name()) == false)
            if (meName.equals(adsName) == false)
                return new ResponseEntity<AdsDto>(HttpStatus.FORBIDDEN);

        if (adsEntity != null) {
            //            adsEntity.setAuthor(adsEntity1.getAuthor());
            adsEntity.setPrice(adsEntity1.getPrice());
            adsEntity.setTitle(adsEntity1.getTitle());
            //adsEntity.setImages(adsEntity1.getImages());
            adsService.saveAddAds(adsEntity);

            return ResponseEntity.ok(body);
        } else {
            return new ResponseEntity<AdsDto>(HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping("search{title}")
    public List<AdsEntity> findAllByTitleLike(@PathVariable("title") String title) {
        return adsService.findAllByTitleLike(title);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PatchMapping(value = "{ad}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdsImage(Authentication authentication, @PathVariable Integer ad, @RequestParam MultipartFile file) throws IOException {
        AdsEntity adsEntity = adsService.findAds(ad);
        if (adsEntity != null) {
            if (authService.hasRole(authentication.getName(), UserEntity.UserRole.ADMIN.name()) == false)
                if (authentication.getName().equals(adsEntity.getAuthor().getUsername()) == false)
                    return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
            adsImageService.saveAddFile(adsEntity, file);
            return ResponseEntity.ok(null);
        } else {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }

}
