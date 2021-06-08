package com.infrastructure.backend.controller;

import com.infrastructure.backend.configuration.security.auth.TokenHelper;
import com.infrastructure.backend.entity.task.Task;
import com.infrastructure.backend.entity.task.TaskState;
import com.infrastructure.backend.entity.user.User;
import com.infrastructure.backend.model.common.response.CommonResponse;
import com.infrastructure.backend.model.task.request.TaskCreateRequest;
import com.infrastructure.backend.service.TaskService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TokenHelper tokenHelper;

    @ApiOperation(value = "Get the task information", notes = "Return the task information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/find_by_id/{taskId}")
    @ResponseBody
    public ResponseEntity<Task> getTaskById(@RequestHeader("Authorization") String authorization,
                                            @PathVariable(value = "taskId") int taskId) {
        return ResponseEntity.ok(this.taskService.find(taskId));
    }

    @PostMapping(path = "/{projectId}/add")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'ADD_TASK')")
    public ResponseEntity<Task> addNewTask(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "projectId") Integer projectId,
                                           @RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        return ResponseEntity.ok(this.taskService.create(taskCreateRequest));
    }

    @ApiOperation(value = "Update task", notes = "Returns the updated task information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Project does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @PutMapping(path = "/{projectId}/{taskId}/update")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'UPDATE_TASK')")
    public ResponseEntity<Task> updateUser(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "projectId") Integer projectId,
                                           @PathVariable(value = "taskId") Integer taskId,
                                           @RequestBody @Valid TaskCreateRequest taskCreateRequest) {
        return ResponseEntity.ok(this.taskService.update(taskId, taskCreateRequest));
    }

    @ApiOperation(value = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Task does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @DeleteMapping(path = "/{projectId}/{taskId}/delete")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'DELETE_TASK')")
    public ResponseEntity<CommonResponse> deleteTask(@RequestHeader("Authorization") String authorization,
                                                        @PathVariable(value = "projectId") Integer projectId,
                                                        @PathVariable(value = "taskId") Integer taskId) {

        this.taskService.delete(taskId);
        return ResponseEntity.ok(new CommonResponse("Delete success", null));
    }

    @ApiOperation(value = "Find all task by user", notes = "Return the list of tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/find_all_by_user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Task>> findAllTaskByUser(@RequestHeader("Authorization") String authorization,
                                                        @PathVariable(value = "userId") Integer userId) {
        return ResponseEntity.ok(this.taskService.findAllByUser(userId));
    }

    @ApiOperation(value = "Find all task by project", notes = "Return the list of tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/find_all_by_project/{projectId}")
    @ResponseBody
    @PreAuthorize("hasPermission(#projectId, 'FIND_ALL_TASK')")
    public ResponseEntity<List<Task>> findAllByProject(@RequestHeader("Authorization") String authorization,
                                                       @PathVariable(value = "projectId") Integer projectId) {
        return ResponseEntity.ok(this.taskService.findAllByProject(projectId));
    }

    @ApiOperation(value = "Find all my tasks", notes = "Return the list of tasks")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request")})
    @GetMapping(path = "/my_tasks")
    @ResponseBody
    public ResponseEntity<List<Task>> findAllMyTasks(@RequestHeader("Authorization") String authorization) {

        User user = this.tokenHelper.getUserFromToken(this.tokenHelper.getToken(authorization));
        return ResponseEntity.ok(this.taskService.findAllByUser(user.getId()));
    }

    @ApiOperation(value = "Update task state")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Task does not exist."),
            @ApiResponse(code = 400, message = "Bad request")})
    @PutMapping(path = "/{taskId}/update_state")
    @ResponseBody
    public ResponseEntity<Task> changeState(@RequestHeader("Authorization") String authorization,
                                           @PathVariable(value = "taskId") Integer taskId,
                                           @RequestParam(value = "state") String state) {

        return ResponseEntity.ok(this.taskService.changeState(taskId, TaskState.valueOf(state)));
    }
}
