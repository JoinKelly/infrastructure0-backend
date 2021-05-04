package com.infrastructure.backend.controller;

import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.model.user.request.ChangePassword;
import com.infrastructure.backend.model.user.request.UserLogin;
import com.infrastructure.backend.model.user.response.UserTokenState;
import com.infrastructure.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private TokenHelper tokenHelper;

    /**
     * @param userLogin
     * @return a access token and expired time's token
     */
    @ApiOperation(value = "User login", notes = "Returns a access token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> login(@Valid @RequestBody UserLogin userLogin) {
        logger.info(userLogin.getUsername() + " logined");
        return new ResponseEntity<>(this.userService.login(userLogin), HttpStatus.OK);
    }

    @ApiOperation(value = "Get new access token", notes = "Get new access token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @RequestMapping(value = "/refresh_token", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserTokenState> refreshToken(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.userService.refreshToken(this.tokenHelper.getToken(authorization)));
    }

    /**
     * User change his password
     *
     * @param authorization
     * @param changePasswordJson
     * @return
     */
    @ApiOperation(value = "User change password", notes = "User change password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @RequestMapping(path = "/change_password", method = RequestMethod.PUT)
    public ResponseEntity<CommonResponse> changePassword(@RequestHeader("Authorization") String authorization,
                                                         @Valid @RequestBody ChangePassword changePasswordJson) {
        logger.info("User changePassword");
        return new ResponseEntity<>(this.userService.changePassword(this.tokenHelper.getToken(authorization), changePasswordJson), HttpStatus.OK);
    }

}
