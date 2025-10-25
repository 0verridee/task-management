package com.project.taskmanagement.utils;


import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomainTask(
            TaskEntity taskEntity
    ) {

        return new Task(
                taskEntity.getId(),
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                taskEntity.getTaskStatus(),
                taskEntity.getCreateDateTime(),
                taskEntity.getDeadlineDate(),
                taskEntity.getPriority(),
                taskEntity.getDoneDateTime()
        );
    }

    public TaskEntity toEntity(
            Task task
    ) {
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.taskStatus(),
                task.createDateTime(),
                task.deadlineDate(),
                task.priority(),
                task.doneDateTime()
        );
    }


}
