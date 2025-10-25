package com.project.taskmanagement.repository;


import com.project.taskmanagement.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    int countByAssignedUserIdAndTaskStatus(Long assignedUserId, TaskStatus status);
}
