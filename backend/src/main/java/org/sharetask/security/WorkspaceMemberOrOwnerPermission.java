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

import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.WorkspaceRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

/**
 * Check if actually logged in user is member or owner of workspace.
 * @author Michal Bocek
 * @since 1.0.0
 */
public class WorkspaceMemberOrOwnerPermission implements Permission {

	@Setter
	private WorkspaceRepository workspaceRepository;
	
	/* (non-Javadoc)
	 * @see org.sharetask.security.Permission#isAllowed(org.springframework.security.core.Authentication, java.lang.Object)
	 */
	@Override
	public boolean isAllowed(final Authentication authentication, final Object targetDomainObject) {
		boolean result;
		Assert.isTrue(isAuthenticated(authentication), "User is not authenticated!");
		Assert.isTrue(targetDomainObject instanceof Long);
		final Long workspaceId = (Long) targetDomainObject;
		final String userName = authentication.getName();
		final Workspace workspace = workspaceRepository.read(workspaceId);
		if (isWorkspaceOwner(workspace, userName)) {
			result = true;
		} else if (isWorkspaceMember(workspace, userName)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
    private boolean isWorkspaceMember(final Workspace workspace, final String userName) {
    	boolean result = false;
    	for (User member : workspace.getMembers()) {
			if (member.getUsername().equals(userName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean isAuthenticated(final Authentication authentication) {
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    private boolean isWorkspaceOwner(final Workspace workspace, final String userName) {
    	return workspace.getOwner().getUsername().equals(userName);
    }
}
