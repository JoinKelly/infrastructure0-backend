package com.infrastructure.backend.controller;

import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.project.request.ProjectCreateRequest;
import com.infrastructure.backend.model.user.request.UserAddition;
import com.infrastructure.backend.model.user.request.UserUpdateRequest;
import com.infrastructure.backend.service.ProjectService;
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

    @Autowired
    private ProjectService projectService;

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

    @PostMapping(path = "/projects/add")
    @ResponseBody
    public ResponseEntity<Project> addNewProject(@RequestHeader("Authorization") String authorization,
                                           @RequestBody @Valid ProjectCreateRequest projectCreateRequest) {
        return ResponseEntity.ok(this.projectService.create(projectCreateRequest));
    }

    @ApiOperation(value = "Update project", notes = "Returns the updated project information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Project does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @PutMapping(path = "/projects/{projectId}/update")
    @ResponseBody
    public ResponseEntity<Project> updateUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "projectId") Integer projectId,
                                           @RequestBody @Valid ProjectCreateRequest projectUpdateRequest) {
        return ResponseEntity.ok(this.projectService.update(projectId, projectUpdateRequest));
    }

    @ApiOperation(value = "Delete project", notes = "Returns the deleted project information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Project does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping(path = "/projects/{projectId}/delete")
    @ResponseBody
    public ResponseEntity<Project> deleteProject(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "projectId") Integer projectId) {
        return ResponseEntity.ok(this.projectService.delete(projectId));
    }

    @ApiOperation(value = "Find all projects", notes = "Return the list of all projects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/projects/find_all")
    @ResponseBody
    public ResponseEntity<List<Project>> findAllProject(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.projectService.findAll());
    }
}
