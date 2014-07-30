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
package org.sharetask.security;

import lombok.Setter;

import org.sharetask.entity.Task;
import org.sharetask.repository.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * Check if task assignee is same like actually logged in user.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TaskAssigneeOrCreatorPermission implements Permission {

	@Setter
	private TaskRepository taskRepository;
	
	/* (non-Javadoc)
	 * @see org.sharetask.security.Permission#isAllowed(org.springframework.security.core.Authentication,
	 * java.lang.Object)
	 */
	@Override
	public boolean isAllowed(final Authentication authentication, final Object targetDomainObject) {
		boolean result;
		Assert.isTrue(isAuthenticated(authentication), "UserAuthentication is not authenticated!");
		Assert.isTrue(targetDomainObject instanceof Long);
		final Long taskId = (Long) targetDomainObject;
		final String userName = authentication.getName();
		final Task task = this.taskRepository.read(taskId);
		if (task.getAssignee() != null && task.getAssignee().getUsername().equals(userName)) {
			result = true;
		} else if (task.getCreatedBy() !=null && task.getCreatedBy().getUsername().equals(userName)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
    private boolean isAuthenticated(final Authentication authentication) {
		return authentication != null
				&& authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
