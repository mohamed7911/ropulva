package com.ropulva.calendar.application.layer.controller;

import com.ropulva.calendar.business.layer.exception.BusinessException;
import com.ropulva.calendar.business.layer.dtos.TaskDTO;
import com.ropulva.calendar.business.layer.services.TaskService;
import com.ropulva.calendar.business.layer.services.Impl.TasksQuickstart;
import com.ropulva.calendar.data.layer.model.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin("*")
@CommonsLog
class TaskController extends BaseController{

    @Autowired
    private TaskService taskService;

    @PostMapping("/add-task")
    public ResponseEntity<?> addTask(@RequestBody TaskDTO taskDTO) {
        try {
            log.info("Enter addTask api with request body " + taskDTO.toString());
            return success(taskService.addTask(taskDTO));
        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }

    @PutMapping("/update-task")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO){
        try {
            log.info("Enter updateTask api with request body " + taskDTO.toString());

            return success(taskService.updateTask(taskDTO));

        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }

    @GetMapping("/get-tasks/{username}")
    public ResponseEntity<?> getTasks(@PathVariable String username){
        try {
            log.info("Enter getTasks api with PathVariable " + username);

            return success(taskService.getTasks(username));

        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }

    @DeleteMapping("/delete-task")
    public ResponseEntity<?> deleteTask(@RequestBody TaskDTO taskDTO){
        try {
            log.info("Enter deleteTask api with " + taskDTO);

            return success(taskService.deleteTask(taskDTO));

        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }

    @PatchMapping("/sync-task/{username}")
    public ResponseEntity<?> syncTasks(@PathVariable String username){
        try {
            log.info("Enter deleteTask api with " + username);

            return success(taskService.syncTasks(username));

        }catch (BusinessException e){
            return success(e.getErrorObject());
        }
    }
}
