package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.*;
import pro.sky.adsplatform.service.AdsCommentService;
import pro.sky.adsplatform.service.AdsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AdsController {

    private final AdsCommentService adsCommentService;
    private final AdsCommentMapper adsCommentMapper;
    private final ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper;
    private final AdsMapper adsMapper;
    private final CreateAdsMapper createAdsMapper;
    private final AdsService adsService;
    private final ResponseWrapperAdsMapper responseWrapperAdsMapper;
    private final FullAdsMapper fullAdsMapper;


    private static final Logger log = LoggerFactory.getLogger(AdsController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public AdsController(AdsCommentService adsCommentService, AdsCommentMapper adsCommentMapper, ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper, AdsMapper adsMapper, CreateAdsMapper createAdsMapper, AdsService adsService, ResponseWrapperAdsMapper responseWrapperAdsMapper, FullAdsMapper fullAdsMapper, ObjectMapper objectMapper, HttpServletRequest request) {
        this.adsCommentService = adsCommentService;
        this.adsCommentMapper = adsCommentMapper;
        this.responseWrapperAdsCommentMapper = responseWrapperAdsCommentMapper;
        this.adsMapper = adsMapper;
        this.createAdsMapper = createAdsMapper;
        this.adsService = adsService;
        this.responseWrapperAdsMapper = responseWrapperAdsMapper;
        this.fullAdsMapper = fullAdsMapper;
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Operation(
            summary = "addAdsComments",
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "201", description = "Created"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @PostMapping("/ads/{ad_pk}/comment")
    public ResponseEntity<AdsCommentDto> addAdsCommentsUsingPOST(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.DEFAULT, description = "comment", required = true, schema = @Schema()) @Valid @RequestBody AdsCommentDto body) {

        AdsCommentEntity adsCommentEntity = adsCommentMapper.adsCommentDtoToAdsComment(body);
        adsCommentService.saveAddAdsCommentsUsingPOST(adsCommentEntity);

        return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_IMPLEMENTED);
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
    @PostMapping("/ads")
    public ResponseEntity<CreateAdsDto> addAdsUsingPOST(@Parameter(in = ParameterIn.DEFAULT, description = "createAds", required = true, schema = @Schema()) @Valid @RequestBody CreateAdsDto body) {
        AdsEntity adsEntity = createAdsMapper.createAdsDtoToAds(body);
        adsService.saveAddAdsUsingPOST(adsEntity);

        return ResponseEntity.ok(createAdsMapper.adsToCreateAdsDto(adsEntity));
    }

    @Operation(
            summary = "deleteAdsComment",
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @DeleteMapping("/ads/{ad_pk}/comment/{id}")
    public ResponseEntity<Void> deleteAdsCommentUsingDELETE(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));

        if (adsCommentEntity != null) {
            adsCommentService.deleteAdsCommentUsingDELETE(adsCommentEntity);
            return ResponseEntity.ok(null);

        } else {
            throw new NotFoundException("Комментарии отсутствуют");
        }
    }


    @Operation(
            summary = "getALLAds",
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/ads")
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
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/ads/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> getAdsCommentUsingGET(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));

        if (adsCommentEntity != null) {
            return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentEntity));
        } else {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "getAdsComments",
            description = "",
            tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "*/*", schema = @Schema(implementation = ResponseWrapperAdsCommentDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema()))
    }
    )
    @GetMapping("/ads/{ad_pk}/comment")
    public ResponseEntity<ResponseWrapperAdsCommentDto> getAdsCommentsUsingGET(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk) {
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
    @GetMapping("/ads/me")
    public ResponseEntity<ResponseWrapperAdsDto> getAdsMeUsingGET(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "authenticated", required = false) Boolean authenticated, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "authorities[0].authority", required = false) String authorities0Authority, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "credentials", required = false) Object credentials, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "details", required = false) Object details, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "principal", required = false) Object principal) {
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
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "*/*", schema = @Schema(implementation = FullAdsDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/ads/{id}")
    public ResponseEntity<FullAdsDto> getAdsUsingGET(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {

        AdsEntity adsEntity = adsService.getAds(id);
        return ResponseEntity.ok(fullAdsMapper.adsToFullAdsDto(adsEntity));

    }


    @Operation(
            summary = "removeAds",
            description = "",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @DeleteMapping("/ads/{id}")
    public ResponseEntity<Void> removeAdsUsingDELETE(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        AdsEntity adsEntity = adsService.getAds(id);
        if (adsEntity != null) {
            adsService.removeAdsUsingDELETE(adsEntity);
            return ResponseEntity.ok(null);

        } else {
            throw new NotFoundException("Нечего удалчть");
        }
    }

    @Operation(
            summary = "updateAdsComment",
            description = "Добавить объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @RequestMapping(value = "/ads/{ad_pk}/comment/{id}",
            produces = {"*/*"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    //@PutMapping("/ads/{ad_pk}/comment/{id}")
    public ResponseEntity<AdsCommentDto> updateAdsCommentUsingPATCH(@Parameter(in = ParameterIn.PATH, description = "ad_pk", required = true, schema = @Schema()) @PathVariable("ad_pk") String adPk, @Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id, @Parameter(in = ParameterIn.DEFAULT, description = "comment", required = true, schema = @Schema()) @Valid @RequestBody AdsCommentDto body) {

        AdsCommentEntity adsCommentEntity = adsCommentService.getAdsComment(id, Long.parseLong(adPk));
        AdsCommentEntity adsCommentEntity1 = adsCommentMapper.adsCommentDtoToAdsComment(body);

        if (adsCommentEntity != null) {
            adsCommentEntity = adsCommentEntity1;

            return ResponseEntity.ok(adsCommentMapper.adsCommentToAdsCommentDto(adsCommentEntity));
        } else {
            return new ResponseEntity<AdsCommentDto>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "updateAds",
            description = "Добавить объявления",
            tags = {"Объявления"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK"),
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            }
    )
    @RequestMapping(value = "/ads/{id}",
            produces = {"*/*"},
            consumes = {"application/json"},
            method = RequestMethod.PATCH)
    //@PutMapping("/updateAds")
    public ResponseEntity<AdsDto> updateAdsUsingPATCH(@Parameter(in = ParameterIn.PATH, description = "id", required = true, schema = @Schema()) @PathVariable("id") Integer id, @Parameter(in = ParameterIn.DEFAULT, description = "ads", required = true, schema = @Schema()) @Valid @RequestBody AdsDto body) {

        AdsEntity adsEntity = adsService.getAds(id);
        AdsEntity adsEntity1 = adsMapper.adsDtoToAds(body);

        if (adsEntity != null) {
            adsEntity = adsEntity1;

            return ResponseEntity.ok(adsMapper.adsToAdsDto(adsEntity));
        } else {
            return new ResponseEntity<AdsDto>(HttpStatus.NOT_FOUND);
        }

    }

}
