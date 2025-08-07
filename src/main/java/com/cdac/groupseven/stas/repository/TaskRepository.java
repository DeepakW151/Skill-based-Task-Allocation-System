package com.cdac.groupseven.stas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.groupseven.stas.entity.Task;
import com.cdac.groupseven.stas.entity.User;
import com.cdac.groupseven.stas.entity.UserTask;
import com.cdac.groupseven.stas.enums.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

	int countByStatus(TaskStatus status);
	

}