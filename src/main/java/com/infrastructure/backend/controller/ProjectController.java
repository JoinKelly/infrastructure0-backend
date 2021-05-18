package com.infrastructure.backend.controller;

import com.infrastructure.backend.entity.project.Project;
import com.infrastructure.backend.service.ProjectService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

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
}
