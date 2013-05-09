/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.sharetask.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.TaskQueue;
import org.sharetask.api.TaskService;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.entity.Comment;
import org.sharetask.entity.Event;
import org.sharetask.entity.Event.EventType;
import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.Task.StateType;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.TaskRepository;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

	@Inject
	private TaskRepository taskRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private WorkspaceRepository workspaceRepository;

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#createTask(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	public TaskDTO createTask(final Long workspaceId, final TaskDTO task) {
		// sanity check if exist workspace
		final Workspace workspace = workspaceRepository.findOne(workspaceId);
		if (workspace == null) {
			log.error("Workspace with id: {} doesn't exists.", workspaceId);
			throw new EntityNotFoundException("Workspace entity doesn't exists for id: " + workspaceId);
		}
		
		Task taskEntity = DTOConverter.convert(task, Task.class);
		taskEntity.setWorkspace(workspace);
		taskEntity = taskRepository.save(taskEntity);
		return DTOConverter.convert(taskEntity, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#addComment(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public TaskDTO addComment(final Long taskId, final String message) {
		// sanity check if exist workspace
		final Task task = taskRepository.findOne(taskId);
		if (task == null) {
			log.error("Task with id: {} doesn't exists.", taskId);
			throw new EntityNotFoundException("Task entity doesn't exists for id: " + taskId);
		}
		
		task.addComment(new Comment(message));
		taskRepository.save(task);
		return DTOConverter.convert(task, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#findTaskByQueue(java.lang.Long, org.sharetask.api.TaskQueue)
	 */
	@Override
	public List<TaskDTO> findTaskByQueue(final Long workspaceId, final TaskQueue taskQueue) {
		List<Task> result;
		switch (taskQueue) {
			case ALL:
				result = taskRepository.findByWorkspaceId(workspaceId);
				break;
			case EXPIRED:
				result = taskRepository.findByDueDateLessThan(new Date());
				break;
			case HIGH_PRIORITY:
				result = taskRepository.findByPriority(PriorityType.HIGH);
				break;
			case FINISHED:
				result = taskRepository.findByState(StateType.FINISHED);
				break;
			case TODAY:
				//TODO 
				result = taskRepository.findByDueDate(new Date());
				break;
			default:
				result = new ArrayList<Task>();
		}
		return DTOConverter.convertList(result, TaskDTO.class);
	}
	
	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#updateTask(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	public TaskDTO updateTask(TaskDTO task) {
		Task entity = taskRepository.findOne(task.getId());
		DTOConverter.convert(task, entity);
		entity = taskRepository.save(entity);
		return DTOConverter.convert(entity, TaskDTO.class);
	}
	
	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#completeTask(java.lang.Long)
	 */
	@Override
	@Transactional
	public void completeTask(Long taskId) {
		Task entity = taskRepository.findOne(taskId);
		entity.finish();
		taskRepository.save(entity);
	}
	
	@Override
	public void forwardTask(Long taskId, List<String> owners) {
		Task task = taskRepository.findOne(taskId);
		if (task == null) {
			log.error("Task with id: {} doesn't exists.", taskId);
			throw new EntityNotFoundException("Task entity doesn't exists for id: " + taskId);
		}

		task.getOwners().clear();
		Iterable<User> ownerList = userRepository.findAll(owners);
		for (User user : ownerList) {
			if (task.getWorkspace().getMembers().contains(user)) {
				log.debug("Added user {} is member of workspace.", user.getUsername());
				task.getOwners().add(user);
			}
		}
		
		task.addEvent(new Event(EventType.TASK_FORWARDED));
		taskRepository.save(task);
	}
}
