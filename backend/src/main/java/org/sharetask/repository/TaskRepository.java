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
package org.sharetask.repository;

import java.util.Date;
import java.util.List;

import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.Task.StateType;
import org.sharetask.repository.base.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository definition for task structure.
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface TaskRepository extends BaseJpaRepository<Task, Long>, TaskRepositoryCustom {

	/**
	 * Get all tasks for specified workspace.
	 * @param workspaceId
	 * @return
	 */
	List<Task> findByWorkspaceId(final Long workspaceId);

	/**
	 * Get all tasks which was created by specified user or are assigned to specified user.
	 * @param workspaceId workspace identifier
	 * @param userName user name used in query
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND (t.createdBy.username = ?2 OR t.assignee.username = ?2)")
	List<Task> findByUsername(final long workspaceId, final String userName);
	
	/**
	 * Get all task for specified workspace regarding priority.
	 * @param workspaceId workspace identifier
	 * @param priority demanded priority
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.priority = ?2")
	List<Task> findByPriority(final long workspaceId, final PriorityType priority);
	
	/**
	 * Get all tasks for workspace which due date is equal to specified one.
	 * @param workspaceId workspace identifier
	 * @param date date used for searching
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.dueDate = ?2")
	List<Task> findByDueDate(final long workspaceId, final Date date);

	/**
	 * Get all tasks for workspace which due date is less than specified.
	 * @param workspaceId workspace identifier
	 * @param date date used for searching
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.dueDate < ?2")
	List<Task> findByDueDateLessThan(final long workspaceId, final Date date);

	/**
	 * Get all tasks for workspace in specified state.
	 * @param workspaceId workspace identifier
	 * @param state task state
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.state = ?2")
	List<Task> findByState(final long workspaceId, final StateType state);

	/**
	 * Get all tasks for specified user.
	 * @param username user name
	 * @return task list
	 */
	@Query("SELECT t FROM Task t WHERE t.createdBy.username = ?1 OR t.assignee.username = ?1")
	List<Task> findAllUserTaks(final String username);

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	@Query("SELECT count(t) FROM Task t")
	Long getTotalCount();

	/**
	 * Gets the count created after.
	 *
	 * @param date the date
	 * @return the count created after
	 */
	@Query("SELECT count(t) FROM Task t WHERE t.createdOn > ?1")
	Long getCountCreatedAfter(Date date);
}
