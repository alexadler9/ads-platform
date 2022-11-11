package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.*;
import pro.sky.adsplatform.service.AdsCommentService;
import pro.sky.adsplatform.service.AdsImageService;
import pro.sky.adsplatform.service.AdsService;

import javax.naming.NotContextException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsCommentService adsCommentService;
    private final AdsCommentMapper adsCommentMapper;
    private final ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper;
    private final AdsMapper adsMapper;
    private final CreateAdsMapper createAdsMapper;
    private final AdsService adsService;
    private final AdsImageService adsImageService;
    private final ResponseWrapperAdsMapper responseWrapperAdsMapper;
    private final FullAdsMapper fullAdsMapper;


    private static final Logger log = LoggerFactory.getLogger(AdsController.class);

    private final ObjectMapper objectMapper;

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
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<AdsDto> addAdsUsingPOST(
            @RequestPart("properties") CreateAdsDto body,
            @RequestPart("image") MultipartFile file) {

//        @PostMapping ( value = "", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
//    public ResponseEntity<AdsDto> addAdsUsingPOST(
//
//            @Valid @RequestPart("properties") @Parameter(schema = @Schema()) AdsDto requestDto ,
//            @RequestPart("image") @Parameter(schema = @Schema(type = "string",format= "binary")) MultipartFile imageFile){

        AdsEntity adsEntity = createAdsMapper.createAdsDtoToAds(body);

//Part 1
        adsService.saveAddAds(adsEntity);

//Part2
        //       adsImageService.saveAddFile(adsEntity, file);

        return ResponseEntity.ok(adsMapper.adsToAdsDto(adsEntity));

    }

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AdsController(AdsCommentService adsCommentService, AdsCommentMapper adsCommentMapper, ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper, AdsMapper adsMapper, CreateAdsMapper createAdsMapper, AdsService adsService, AdsImageService adsImageService, ResponseWrapperAdsMapper responseWrapperAdsMapper, FullAdsMapper fullAdsMapper, ObjectMapper objectMapper, HttpServletRequest request) {
        this.adsCommentService = adsCommentService;
        this.adsCommentMapper = adsCommentMapper;
        this.responseWrapperAdsCommentMapper = responseWrapperAdsCommentMapper;
        this.adsMapper = adsMapper;
        this.createAdsMapper = createAdsMapper;
        this.adsService = adsService;
        this.adsImageService = adsImageService;
        this.responseWrapperAdsMapper = responseWrapperAdsMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.objectMapper = objectMapper;
        this.request = request;
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
    @PostMapping("{ad_pk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsCommentsUsingPOST(@PathVariable("ad_pk") String adPk, @RequestBody AdsCommentDto body) {
        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(body);
        AdsEntity ads = adsService.findAds(Long.parseLong(adPk));
        if (ads == null) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }
        adsComment.setAds(ads);
        adsCommentService.createAdsComment(adsComment);
        return ResponseEntity.ok(body);

//        try {
//            adsCommentEntity.setAds(adsService.findAds(Long.parseLong(adPk)));
//            adsCommentService.saveAddAdsCommentsUsingPOST(adsCommentEntity);
//            return ResponseEntity.ok(body);
//        } catch (NotFoundException e) {
//            throw new NotFoundException("Не сохранили");
//        }
    }

    //IMAGE
    @GetMapping("image/{no}")
    public ResponseEntity<byte[]> getImage(@PathVariable("adsImageId") Long adsImageId) {
        AdsImageEntity adsImageEntity = adsImageService.getImageEntity(adsImageId);
        if (adsImageEntity == null) throw new org.webjars.NotFoundException("Не найдена картинка");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE));
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
    @DeleteMapping("{ad_pk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsCommentUsingDELETE(@PathVariable("ad_pk") String adPk, @PathVariable("id") Integer id) {
        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));

        if (adsCommentEntity != null) {
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
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("*/*")) {
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
        return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_IMPLEMENTED);

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
    @GetMapping("me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMeUsingGET(@RequestParam(required = false) Boolean authenticated,
                                                                  @RequestParam(required = false) String authorities0Authority,
                                                                  @RequestParam(required = false) Object credentials,
                                                                  @RequestParam(required = false) Object details,
                                                                  @RequestParam(required = false) Object principal) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ResponseWrapperAdsDto>(objectMapper.readValue("{\n  \"count\" : 0,\n  \"results\" : [ {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  }, {\n    \"image\" : \"image\",\n    \"author\" : 6,\n    \"price\" : 5,\n    \"pk\" : 1,\n    \"title\" : \"title\"\n  } ]\n}", ResponseWrapperAdsDto.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ResponseWrapperAdsDto>(HttpStatus.NOT_IMPLEMENTED);
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
            return ResponseEntity.ok(fullAdsMapper.adsToFullAdsDto(adsEntity));
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
    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeAdsUsingDELETE(@PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        AdsEntity adsEntity = adsService.findAds(id);
        if (adsEntity != null) {
            adsService.removeAdsUsingDELETE(adsEntity);
            return ResponseEntity.ok(null);
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
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
    @RequestMapping(value = "{ad_pk}/comment/{id}",
            produces = {"*/*"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    //@PutMapping("/ads/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsCommentUsingPATCH(@PathVariable("ad_pk") String adPk, @PathVariable("id") Integer id, @RequestBody AdsCommentDto body) {
        try {
            AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(body);
            adsCommentService.updateAdsCommentText(adsComment, id, Long.parseLong(adPk));
        } catch (IllegalArgumentException | NotFoundException e) {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(body);

//        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));
//        AdsCommentEntity adsCommentEntity1 = adsCommentMapper.adsCommentDtoToAdsComment(body);
//
//
//        if (adsCommentEntity != null) {
//            adsCommentEntity.setAuthor(adsCommentEntity1.getAuthor());
//
//            Integer pk = body.getPk();
//            AdsEntity adsEntity = adsService.findAds(pk);
//            adsCommentEntity.setAds(adsEntity);
//
//            //            adsCommentEntity.setAds(adsCommentEntity1.getAds());
//            adsCommentEntity.setDateTime(adsCommentEntity1.getDateTime());
//            adsCommentEntity.setText(adsCommentEntity1.getText());
//
//            adsCommentService.saveAddAdsCommentsUsingPOST(adsCommentEntity);
//            AdsCommentDto adsCommentDto = adsCommentMapper.adsCommentToAdsCommentDto(adsCommentEntity);
//            return new ResponseEntity(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<AdsCommentDto>(HttpStatus.NO_CONTENT);
//        }
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
    @RequestMapping(value = "{id}",
            produces = {"*/*"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    //@PutMapping("/updateAds")
    public ResponseEntity<AdsDto> updateAdsUsingPATCH(@PathVariable("id") Integer id, @RequestBody AdsDto body) {

        AdsEntity adsEntity = adsService.findAds(id);
        AdsEntity adsEntity1 = adsMapper.adsDtoToAds(body);

        if (adsEntity != null) {
 adsEntity.setAuthor(adsEntity1.getAuthor());
 adsEntity.setPrice(adsEntity1.getPrice());
 adsEntity.setTitle(adsEntity1.getTitle());
 adsEntity.setImages(adsEntity1.getImages());
             adsService.saveAddAds(adsEntity);

            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<AdsDto>(HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping("search{title}")
    public List<AdsEntity> findAllByTitleLike(@PathVariable("title") String title) {
        return adsService.findAllByTitleLike(title);
    }
}
