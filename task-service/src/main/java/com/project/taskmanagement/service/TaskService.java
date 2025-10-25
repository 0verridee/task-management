package com.project.taskmanagement.service;


import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.model.TaskStatus;
import com.project.taskmanagement.repository.TaskEntity;
import com.project.taskmanagement.repository.TaskRepository;
import com.project.taskmanagement.utils.TaskMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final RestClient restClient;

    public Task findTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found Task with id= " + id));

        return taskMapper.toDomainTask(task);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDomainTask)
                .toList();
    }

    public Task createTask(Task taskToCreate) {
        if (taskToCreate.taskStatus() != null) {
            throw new IllegalArgumentException("Status should be empty!");
        }

        validateUserExists(taskToCreate.creatorId());
        validateUserExists(taskToCreate.assignedUserId());

        var entityToSave = taskMapper.toEntity(taskToCreate);

        entityToSave.setTaskStatus(TaskStatus.CREATED);
        entityToSave.setCreateDateTime(LocalDate.now());

        if (entityToSave.getDeadlineDate().isBefore(entityToSave.getCreateDateTime())) {
            throw new IllegalArgumentException("Deadline date должен быть после даты создания");
        }

        var updatedEntity = taskRepository.save(entityToSave);
        return taskMapper.toDomainTask(updatedEntity);
    }

    public void deleteTask(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found Task with id= " + id));

        taskRepository.delete(task);

        log.info("Task with id= {} was deleted", id);
    }

    public Task updateTask(Long id, Task taskToUpdate) {
        TaskEntity existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Не найдена задача с id=" + id));

        if (!taskToUpdate.deadlineDate().isAfter(taskToUpdate.createDateTime())) {
            throw new IllegalArgumentException("Deadline date должен быть после Create date");
        }

        validateUserExists(taskToUpdate.creatorId());
        validateUserExists(taskToUpdate.assignedUserId());


        if (existingTask.getTaskStatus() == TaskStatus.DONE) {

            boolean isOnlyStatusChange = taskToUpdate.taskStatus() == TaskStatus.IN_PROGRESS &&
                    taskToUpdate.createDateTime().equals(existingTask.getCreateDateTime()) &&
                    taskToUpdate.deadlineDate().equals(existingTask.getDeadlineDate()) &&
                    Objects.equals(taskToUpdate.assignedUserId(), existingTask.getAssignedUserId()) &&
                    Objects.equals(taskToUpdate.creatorId(), existingTask.getCreatorId());

            if (isOnlyStatusChange) {
                existingTask.setTaskStatus(TaskStatus.IN_PROGRESS);
                existingTask.setDoneDateTime(null);
            } else {
                throw new IllegalStateException("Cannot modify task! status: " + existingTask.getTaskStatus());
            }
        } else {
            existingTask.setCreatorId(taskToUpdate.creatorId());
            existingTask.setAssignedUserId(taskToUpdate.assignedUserId());
            existingTask.setTaskStatus(taskToUpdate.taskStatus());
            existingTask.setDeadlineDate(taskToUpdate.deadlineDate());

            if (taskToUpdate.taskStatus() != TaskStatus.DONE) {
                existingTask.setDoneDateTime(null);
            }
        }

        TaskEntity savedTask = taskRepository.save(existingTask);
        return taskMapper.toDomainTask(savedTask);
    }

    @Transactional
    public Task startTask(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found Task with id= " + id));

        if(task.getAssignedUserId() == null) {
            throw new IllegalArgumentException("Task cannot be started, assignedUserId is null!");
        }

        validateUserExists(task.getAssignedUserId());

        Long assignedUserId = task.getAssignedUserId();
        int taskCount = taskRepository.countByAssignedUserIdAndTaskStatus(assignedUserId,TaskStatus.IN_PROGRESS);

        if(taskCount >= 4) {
            throw new IllegalStateException("Limit exceeded (4) with active tasks with userId= " + assignedUserId);
        }

        task.setTaskStatus(TaskStatus.IN_PROGRESS);

        var savedTask = taskRepository.save(task);

        return taskMapper.toDomainTask(savedTask);
    }

    @Transactional
    public Task completeTask(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Not found Task with id= " + id));

        if(task.getAssignedUserId() == null || task.getDeadlineDate() == null) {
            throw new IllegalArgumentException("Task cannot be done!");
        }

        validateUserExists(task.getAssignedUserId());

        task.setTaskStatus(TaskStatus.DONE);
        task.setDoneDateTime(LocalDateTime.now());

        var savedTask = taskRepository.save(task);
        return taskMapper.toDomainTask(savedTask);
    }


    private void validateUserExists(Long userId) {
        if(userId == null) {
            return;
        }
        try {
            restClient.get()
                    .uri("http://user-api:8080/users/{userId}", userId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new NoSuchElementException("User with id= " + userId + " not found!");
        }
    }
}
