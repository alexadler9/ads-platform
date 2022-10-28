package pro.sky.adsplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.adsplatform.dto.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UserController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @PostMapping("/addUser")
    public ResponseEntity<CreateUser> addUserUsingPOST(@Parameter(in = ParameterIn.DEFAULT, description = "user", required=true, schema=@Schema()) @Valid @RequestBody CreateUser body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<CreateUser>(objectMapper.readValue("{\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"password\" : \"password\",\n  \"phone\" : \"phone\",\n  \"email\" : \"email\"\n}", CreateUser.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<CreateUser>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<CreateUser>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUserUsingGET(@Parameter(in = ParameterIn.PATH, description = "id", required=true, schema=@Schema()) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"phone\" : \"phone\",\n  \"id\" : 6,\n  \"email\" : \"email\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<ResponseWrapperUser> getUsersUsingGET() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ResponseWrapperUser>(objectMapper.readValue("{\n  \"count\" : 0,\n  \"results\" : [ {\n    \"firstName\" : \"firstName\",\n    \"lastName\" : \"lastName\",\n    \"phone\" : \"phone\",\n    \"id\" : 6,\n    \"email\" : \"email\"\n  }, {\n    \"firstName\" : \"firstName\",\n    \"lastName\" : \"lastName\",\n    \"phone\" : \"phone\",\n    \"id\" : 6,\n    \"email\" : \"email\"\n  } ]\n}", ResponseWrapperUser.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ResponseWrapperUser>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ResponseWrapperUser>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PostMapping("/setPassword")
    public ResponseEntity<NewPassword> setPasswordUsingPOST(@Parameter(in = ParameterIn.DEFAULT, description = "newPassword", required=true, schema=@Schema()) @Valid @RequestBody NewPassword body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<NewPassword>(objectMapper.readValue("{\n  \"newPassword\" : \"newPassword\",\n  \"currentPassword\" : \"currentPassword\"\n}", NewPassword.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<NewPassword>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<NewPassword>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUserUsingPATCH(@Parameter(in = ParameterIn.DEFAULT, description = "user", required=true, schema=@Schema()) @Valid @RequestBody User body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"firstName\" : \"firstName\",\n  \"lastName\" : \"lastName\",\n  \"phone\" : \"phone\",\n  \"id\" : 6,\n  \"email\" : \"email\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

}
