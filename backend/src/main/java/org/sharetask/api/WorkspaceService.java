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

import org.sharetask.api.dto.WorkspaceDTO;
import org.springframework.validation.annotation.Validated;

/**
 * Service for accessing workspace.
 * @author Michal Bocek
 * @since 1.0.0
 */
@Validated
public interface WorkspaceService {

	/**
	 * Create new workspace
	 * @param workspace
	 * @return
	 */
	@NotNull WorkspaceDTO create(@Valid @NotNull WorkspaceDTO workspace);
	
	/**
	 * Update workspace.
	 * @param workspace
	 * @return
	 */
	@NotNull WorkspaceDTO update(@Valid @NotNull WorkspaceDTO workspace);

	/**
	 * Add member to specified workspace.
	 * @param workspaceId
	 * @param userId
	 */
	void addMember(@NotNull Long workspaceId, @NotNull String username);
	
	/**
	 * Remove member from specified workspace.
	 * @param workspaceId
	 * @param userId
	 */
	void removeMember(@NotNull Long workspaceId, @NotNull String username);
	
	/**
	 * Find all workspaces for specified owner.
	 * @param ownerId
	 * @return
	 */
	List<WorkspaceDTO> findByOwner(@NotNull String username);
	
	/**
	 * Find all workspaces for member.
	 * @param username
	 * @return
	 */
	List<WorkspaceDTO> findByMember(@NotNull String username);

	/**
	 * Find all workspaces where i'm owner or member. 
 	 * @param username
	 * @return
	 */
	List<WorkspaceDTO> findAllMyWorkspaces(@NotNull String username);
	
	/**
	 * Find workspace by type.
	 * Expected type is OWNER, MEMBER, ALL_MY.
	 * @param type
	 * @return
	 */
	List<WorkspaceDTO> findByType(@NotNull WorkspaceQueryType queryType);

	/**
	 * Delete whole workspace
	 * @param taskId
	 */
	void delete(@NotNull Long workspaceId);
}
