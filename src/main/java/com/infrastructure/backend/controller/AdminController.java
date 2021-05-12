package com.infrastructure.backend.controller;

import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.common.request.UserAddition;
import com.infrastructure.backend.model.common.request.UserUpdateRequest;
import com.infrastructure.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/users/add")
    @ResponseBody
    public ResponseEntity<User> addNewUser(@RequestHeader("Authorization") String authorization,
                                           @RequestBody @Valid UserAddition user) {
        return ResponseEntity.ok(this.userService.create(user));
    }

    @ApiOperation(value = "Update user", notes = "Returns the updated user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @PutMapping(path = "/users/{userId}/update")
    @ResponseBody
    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "userId") Integer userId,
                                           @RequestBody @Valid UserUpdateRequest user) {
        return ResponseEntity.ok(this.userService.update(userId, user));
    }

    @ApiOperation(value = "Delete user", notes = "Returns the deleted user information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "User does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping(path = "/users/{userId}/delete")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "userId") Integer userId) {
        return ResponseEntity.ok(this.userService.delete(userId));
    }

    @ApiOperation(value = "Find all users", notes = "Return the list of all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/users/find_all")
    @ResponseBody
    public ResponseEntity<List<User>> findAll(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.userService.findAll());
    }
}
