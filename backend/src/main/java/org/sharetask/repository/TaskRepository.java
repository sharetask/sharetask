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
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface TaskRepository extends BaseJpaRepository<Task, Long>, TaskRepositoryCustom {

	List<Task> findByWorkspaceId(final Long workspaceId);

	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND (t.createdBy.username = ?2 OR t.assignee.username = ?2)")
	List<Task> findByUsername(final long workspaceId, final String userName);
	
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.priority = ?2")
	List<Task> findByPriority(final long workspaceId, final PriorityType priority);
	
	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.dueDate = ?2")
	List<Task> findByDueDate(final long workspaceId, final Date date);

	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.dueDate < ?2")
	List<Task> findByDueDateLessThan(final long workspaceId, final Date date);

	@Query("SELECT t FROM Task t WHERE t.workspace.id = ?1 AND t.state = ?2")
	List<Task> findByState(final long workspaceId, final StateType state);

	@Query("SELECT t FROM Task t WHERE t.createdBy.username = ?1 OR t.assignee.username = ?1")
	List<Task> findAllUserTaks(final String username);
}
