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
package org.sharetask.api;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.sharetask.api.dto.CommentDTO;
import org.sharetask.api.dto.EventDTO;
import org.sharetask.api.dto.TaskDTO;
import org.springframework.validation.annotation.Validated;

/**
 * Service for accessing tasks.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Validated
public interface TaskService {

	/**
	 * Create new task.
	 * @param task
	 * @return
	 */
	@NotNull TaskDTO create(@NotNull final Long workspaceId, @Valid @NotNull final TaskDTO task);
	
	/**
	 * Add comment to specified task.
	 * @param taskId
	 * @param message
	 * @return
	 */
	@NotNull TaskDTO addComment(@NotNull final Long taskId, @NotNull final String message);
	
	/**
	 * Find task by specified queue.
	 * @param workspaceId
	 * @param taskQueue
	 * @return
	 */
	@NotNull List<TaskDTO> findByQueue(@NotNull final Long workspaceId, @NotNull final TaskQueue taskQueue);
	
	/**
	 * Update task.
	 * @param task
	 * @return
	 */
	@NotNull TaskDTO update(@NotNull @Valid final TaskDTO task);
	
	/**
	 * Complete task.
	 * Set state to FINISH and add FINISHED event to events.
	 * @param taskId
	 */
	void complete(@NotNull final Long taskId);
	
	/**
	 * Forward task to specified group.
	 * @param taskId
	 * @param assignee
	 */
	void forward(@NotNull final Long taskId, @NotNull final String assignee);
	
	/**
	 * Get all comments for task.
	 * @param taskId
	 * @return
	 */
	List<CommentDTO> getComments(@NotNull final Long taskId);

	/**
	 * Get all events for task.
	 * @param taskId
	 * @return
	 */
	List<EventDTO> getEvents(@NotNull final Long taskId);
	
	/**
	 * Delete task with specified id.
	 * @param taskId
	 */
	void delete(@NotNull final Long taskId);

	/**
	 * Get task for id.
	 * @param taskId
	 * @return
	 */
	TaskDTO getTask(@NotNull final Long taskId);
	
	/**
	 * Get all tasks for currently logged in user.
	 * @param taskId
	 * @return
	 */
	List<TaskDTO> getAllMyTasks();

	/**
	 * Renew task. 
	 * Task must be in complete state and then will be change to new state. 
	 * @param taskId
	 */
	void renew(@NotNull final Long taskId); 
}
