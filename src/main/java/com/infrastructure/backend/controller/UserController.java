package com.infrastructure.backend.controller;

import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenHelper tokenHelper;

    @PostMapping(path = "/add")
    @ResponseBody
    public String addNewUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(newUser);
        return "Saved";
    }

    @ApiOperation(value = "Find all users", notes = "Return the list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/find_all")
    @ResponseBody
    public ResponseEntity<List<User>> findAll(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.userRepository.findAll());
    }

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
