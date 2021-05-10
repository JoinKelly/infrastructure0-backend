package com.infrastructure.backend.controller;

import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.user.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private TokenHelper tokenHelper;

    @ApiOperation(value = "Get the current user information", notes = "Return the current user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/user_info")
    @ResponseBody
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.tokenHelper.getUserFromToken(this.tokenHelper.getToken(authorization)));
    }
}
