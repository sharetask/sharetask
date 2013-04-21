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

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class WorkspaceServiceImpl implements WorkspaceService {

	@Inject
	private WorkspaceRepository workspaceRepository;

	@Inject
	private UserRepository userRepository;

	@Override
	@Transactional
	public WorkspaceDTO createWorkspace(final WorkspaceDTO workspace) {
		Workspace workspaceEntity = DTOConverter.convert(workspace, Workspace.class);
		workspaceEntity = workspaceRepository.save(workspaceEntity);
		return DTOConverter.convert(workspaceEntity, WorkspaceDTO.class);
	}

	@Override
	@Transactional
	public void addMemeber(final Long workspaceId, final Long userId) {
		// sanity check if exist specified user
		final User user = userRepository.findOne(userId);
		if (user == null) {
			throw new EntityNotFoundException("User entity doesn't exists for id: " + userId);
		}
		
		// sanity check if exist workspace
		final Workspace workspace = workspaceRepository.findOne(workspaceId);
		if (workspace == null) {
			throw new EntityNotFoundException("Workspace entity doesn't exists for id: " + workspaceId);
		}
		
		// add member to workspace
		if (!workspace.getMemebers().contains(user)) {
			workspace.addMember(user);
			workspaceRepository.save(workspace);
		}		
	}

	@Override
	@Transactional
	public void removeMemeber(final Long workspaceId, final Long userId) {
		// sanity check if exist specified user
		final User user = userRepository.findOne(userId);
		if (user == null) {
			throw new EntityNotFoundException("User entity doesn't exists for id: " + userId);
		}
		
		// sanity check if exist workspace
		final Workspace workspace = workspaceRepository.findOne(workspaceId);
		if (workspace == null) {
			throw new EntityNotFoundException("Workspace entity doesn't exists for id: " + workspaceId);
		}
		
		// add member to workspace
		if (workspace.getMemebers().contains(user)) {
			workspace.removeMember(user);
			workspaceRepository.save(workspace);
		}		
	}

	@Override
	public List<WorkspaceDTO> findWorkspaceByOwner(final Long ownerId) {
		final List<Workspace> workspaces = workspaceRepository.findByOwnerId(ownerId);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}	
}
