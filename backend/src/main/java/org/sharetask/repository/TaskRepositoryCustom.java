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

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public interface TaskRepositoryCustom {
	
	List<Task> findByDueDate(final long workspaceId, final Date date);

	List<Task> findByDueDateLessThan(final long workspaceId, final Date date);

	List<Task> findByPriority(final long workspaceId, final PriorityType priority);
	
	List<Task> findByState(final long workspaceId, final StateType priority);
	
}
