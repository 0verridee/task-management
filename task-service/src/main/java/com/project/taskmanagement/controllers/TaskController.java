package com.project.taskmanagement.controllers;


import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // get all Tasks
    @GetMapping()
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Called getAllTasks()");

        return ResponseEntity.ok(taskService.findAllTasks());
    }

    // get task by ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable("id") Long id)
    {
        log.info("Called getTaskById(): id = {}", id);

        return ResponseEntity.ok(taskService.findTaskById(id));
    }

    // create Task
    @PostMapping()
    public ResponseEntity<Task> createTask(
            @RequestBody @Valid Task taskToCreate
    ) {
        log.info("Called createTask()");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(taskToCreate));
    }

    // update task by id
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @PathVariable("id") Long id,
            @RequestBody Task taskToUpdate
    ) {
        log.info("Called updateTask: id={}, taskToUpdate={}", id,taskToUpdate);

        var updated = taskService.updateTask(id, taskToUpdate);

        return ResponseEntity.ok(updated);
    }

    // delete task by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("id") Long id
    ) {
        log.info("Called deleteTask: id={}", id);

        taskService.deleteTask(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> startTask(
            @PathVariable("id") Long id
    ){
        log.info("Called change task to IN_PRORESS: id={}", id);

        Task startedTask = taskService.startTask(id);
        return ResponseEntity.ok(startedTask);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(
        @PathVariable("id") Long id
    ){
        Task completedTask = taskService.completeTask(id);

        return ResponseEntity.ok(completedTask);
    }



}
