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

import lombok.extern.slf4j.Slf4j;

import org.sharetask.api.Constants;
import org.sharetask.api.TaskQueue;
import org.sharetask.api.TaskService;
import org.sharetask.api.dto.CommentDTO;
import org.sharetask.api.dto.EventDTO;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.entity.Comment;
import org.sharetask.entity.Event;
import org.sharetask.entity.Event.EventType;
import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.Task.StateType;
import org.sharetask.entity.UserInformation;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.TaskRepository;
import org.sharetask.repository.UserInformationRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.sharetask.utility.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Task service implementation.
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
	private UserInformationRepository userRepository;

	@Inject
	private WorkspaceRepository workspaceRepository;

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#create(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERIMISSION_WORKSPACE_MEMBER_OR_OWNER)
	public TaskDTO create(final Long workspaceId, final TaskDTO task) {
		final Workspace workspace = workspaceRepository.read(workspaceId);
		final Task taskEntity = DTOConverter.convert(task, Task.class);
		taskEntity.setWorkspace(workspace);
		// when assignee isn't set from source it will be assigned as signed in user
		if (taskEntity.getAssignee() == null) {
			final UserInformation assignee = userRepository.read(SecurityUtil.getCurrentSignedInUsername());
			taskEntity.setAssignee(assignee);
		}
		final Task storedTaskEntity = taskRepository.save(taskEntity);
		return DTOConverter.convert(storedTaskEntity, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#addComment(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERMISSION_TASK_ASSIGNEE_OR_CREATOR)
	public TaskDTO addComment(final Long taskId, final String message) {
		final Task task = taskRepository.read(taskId);
		task.addComment(new Comment(message));
		taskRepository.save(task);
		return DTOConverter.convert(task, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#findByQueue(java.lang.Long, org.sharetask.api.TaskQueue)
	 */
	@Override
	@PreAuthorize(Constants.PERIMISSION_WORKSPACE_MEMBER_OR_OWNER)
	public List<TaskDTO> findByQueue(final Long workspaceId, final TaskQueue taskQueue) {
		List<Task> result;
		switch (taskQueue) {
			case ALL:
				result = taskRepository.findByWorkspaceId(workspaceId);
				break;
			case ALL_MY:
				result = taskRepository.findByUsername(workspaceId, SecurityUtil.getCurrentSignedInUsername());
				break;
			case EXPIRED:
				result = taskRepository.findByDueDateLessThan(workspaceId, new Date());
				break;
			case HIGH_PRIORITY:
				result = taskRepository.findByPriority(workspaceId, PriorityType.HIGH);
				break;
			case FINISHED:
				result = taskRepository.findByState(workspaceId, StateType.FINISHED);
				break;
			case TODAY:
				result = taskRepository.findByDueDate(workspaceId, new Date());
				break;
			default:
				result = new ArrayList<Task>();
		}
		return DTOConverter.convertList(result, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#update(org.sharetask.api.dto.TaskDTO)
	 */
	@Override
	@Transactional
	@PreAuthorize("isAuthenticated() and hasRole('ROLE_USER') and hasPermission(#task.id, 'isTaskAssigneeOrCreator')")
	public TaskDTO update(final TaskDTO task) {
		final Task entity = taskRepository.findOne(task.getId());
		DTOConverter.convert(task, entity);
		final Task storedEntity = taskRepository.save(entity);
		return DTOConverter.convert(storedEntity, TaskDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#complete(java.lang.Long)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERMISSION_TASK_ASSIGNEE)
	public void complete(final Long taskId) {
		final Task entity = taskRepository.findOne(taskId);
		entity.finish();
		taskRepository.save(entity);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#forward(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERMISSION_TASK_ASSIGNEE)
	public void forward(final Long taskId, final String assignee) {
		final Task task = taskRepository.read(taskId);

		final UserInformation assigneeUser = userRepository.read(assignee);
		if (task.getWorkspace().getMembers().contains(assigneeUser)) {
			log.debug("Added user {} is member of workspace.", assigneeUser.getUsername());
			task.setAssignee(assigneeUser);
		}

		task.addEvent(new Event(EventType.TASK_FORWARDED));
		taskRepository.save(task);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#getComments(java.lang.Long)
	 */
	// TODO: security check???
	@Override
	public List<CommentDTO> getComments(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		return DTOConverter.convertList(task.getComments(), CommentDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#getEvents(java.lang.Long)
	 */
	// TODO: security check???
	@Override
	public List<EventDTO> getEvents(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		return DTOConverter.convertList(task.getEvents(), EventDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.TaskService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERMISSION_TASK_ASSIGNEE_OR_CREATOR)
	public void delete(final Long taskId) {
		taskRepository.delete(taskId);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.sharetask.api.TaskService#getTask(java.lang.Long)
	 */
	// TODO: security check???
	@Override
	public TaskDTO getTask(final Long taskId) {
		final Task task = taskRepository.read(taskId);
		return DTOConverter.convert(task, TaskDTO.class);
	}

	@Override
	public List<TaskDTO> getAllMyTasks() {
		final String username = SecurityUtil.getCurrentSignedInUsername();
		final List<Task> task = taskRepository.findAllUserTaks(username);
		return DTOConverter.convertList(task, TaskDTO.class);
	}
	
}
