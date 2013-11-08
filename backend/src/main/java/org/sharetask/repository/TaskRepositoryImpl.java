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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.Task.StateType;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TaskRepositoryImpl implements TaskRepositoryCustom {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<Task> findByDueDate(final long workspaceId, final Date date) {
		final TypedQuery<Task> query = entityManager.createNamedQuery(Task.QUERY_NAME_FIND_BY_DUE_DATE, 
				Task.class);
		query.setParameter("dueDate", date, TemporalType.DATE);
		query.setParameter("workspaceId", workspaceId);
		return query.getResultList();
	}

	@Override
	public List<Task> findByDueDateLessThan(final long workspaceId, final Date date) {
		final TypedQuery<Task> query = entityManager.createNamedQuery(Task.QUERY_NAME_FIND_BY_DUE_DATE_LESS_THAN, 
				Task.class);
		query.setParameter("dueDate", date, TemporalType.DATE);
		query.setParameter("workspaceId", workspaceId);
		return query.getResultList();
	}

	@Override
	public List<Task> findByPriority(final long workspaceId, final PriorityType priority) {
		final TypedQuery<Task> query = entityManager.createNamedQuery(Task.QUERY_NAME_FIND_BY_PRIORITY, 
				Task.class);
		query.setParameter("priority", priority);
		query.setParameter("workspaceId", workspaceId);
		return query.getResultList();
	}

	@Override
	public List<Task> findByState(final long workspaceId, final StateType state) {
		final TypedQuery<Task> query = entityManager.createNamedQuery(Task.QUERY_NAME_FIND_BY_STATE, 
				Task.class);
		query.setParameter("state", state);
		query.setParameter("workspaceId", workspaceId);
		return query.getResultList();
	}

}
