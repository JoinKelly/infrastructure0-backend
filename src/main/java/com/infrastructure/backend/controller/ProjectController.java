package com.infrastructure.backend.controller;

import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.entity.project.ProjectMember;
import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TokenHelper tokenHelper;

    @ApiOperation(value = "Get the project information", notes = "Return the project information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/find_by_id/{projectId}")
    @ResponseBody
    public ResponseEntity<Project> getUserById(@RequestHeader("Authorization") String authorization,
                                               @PathVariable(value = "projectId") int projectId) {
        return ResponseEntity.ok(this.projectService.find(projectId));
    }

    @ApiOperation(value = "Add member to the project", notes = "Return the project member information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/project/{projectId}/add_member/{userId}")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'ADD_MEMBER')")
    public ResponseEntity<ProjectMember> addProjectMember(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable(value = "projectId") int projectId,
                                                          @PathVariable(value = "userId") int userId
    ) {
        return ResponseEntity.ok(this.projectService.addProjectMember(projectId, userId));
    }

    @ApiOperation(value = "Add member to the project", notes = "Return the project member information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/project/{projectId}/add_member_by_email")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'ADD_MEMBER')")
    public ResponseEntity<ProjectMember> addProjectMemberByEmail(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable(value = "projectId") int projectId,
                                                          @RequestParam(value = "email") String email
    ) {
        return ResponseEntity.ok(this.projectService.addProjectMemberByEmail(projectId, email));
    }

    @ApiOperation(value = "Add many members to the project", notes = "Return the project members information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/project/{projectId}/add_many_member")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'ADD_MEMBER')")
    public ResponseEntity<List<ProjectMember>> addManyProjectMembers(@RequestHeader("Authorization") String authorization,
                                                          @PathVariable(value = "projectId") int projectId,
                                                          @RequestParam(value = "userIds") List<Integer> userIds
    ) {
        List<ProjectMember> projectMembers = new ArrayList<>();
        for (Integer userId: userIds) {
            projectMembers.add(this.projectService.addProjectMember(projectId, userId));
        }
        return ResponseEntity.ok(projectMembers);
    }

    @ApiOperation(value = "Remove member to the project", notes = "Return the project member information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/project/{projectId}/remove_member/{userId}")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'REMOVE_MEMBER')")
    public ResponseEntity<CommonResponse> removeProjectMember(@RequestHeader("Authorization") String authorization,
                                                              @PathVariable(value = "projectId") int projectId,
                                                              @PathVariable(value = "userId") int userId
    ) {
        this.projectService.deleteProjectMember(projectId, userId);
        return ResponseEntity.ok(new CommonResponse("Remove member success", null));
    }

    @ApiOperation(value = "Find all members of the project", notes = "Return all members information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/project/{projectId}/all_member")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'FIND_ALL_MEMBER')")
    public ResponseEntity<List<ProjectMember>> findAllProjectMembers(@RequestHeader("Authorization") String authorization,
                                                                    @PathVariable(value = "projectId") int projectId
    ) {
        return ResponseEntity.ok(this.projectService.findAllProjectMembersByProject(projectId));
    }

    @ApiOperation(value = "Find all my projects", notes = "Return the list of all my projects")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/my_projects")
    @ResponseBody
    public ResponseEntity<List<Project>> findAllMyProjects(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(this.projectService.findAllMyProjects(this.tokenHelper.getUserFromToken(this.tokenHelper.getToken(authorization))));
    }
}
