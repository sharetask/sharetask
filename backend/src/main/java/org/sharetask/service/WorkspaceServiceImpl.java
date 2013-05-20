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
import java.util.List;

import javax.inject.Inject;

import org.sharetask.api.WorkspaceQueryType;
import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.springframework.security.core.context.SecurityContextHolder;
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
	public WorkspaceDTO create(final WorkspaceDTO workspace) {
		Workspace workspaceEntity = DTOConverter.convert(workspace, Workspace.class);
		// load owner
		User owner = userRepository.findOne(workspace.getOwner().getUsername());
		workspaceEntity.setOwner(owner);
		// store workspace
		workspaceEntity = workspaceRepository.save(workspaceEntity);
		return DTOConverter.convert(workspaceEntity, WorkspaceDTO.class);
	}

	@Override
	@Transactional
	public WorkspaceDTO update(final WorkspaceDTO workspace) {
		Workspace workspaceEntity = workspaceRepository.read(workspace.getId());
		DTOConverter.convert(workspace, workspaceEntity);
		workspaceEntity = workspaceRepository.save(workspaceEntity);
		return DTOConverter.convert(workspaceEntity, WorkspaceDTO.class);
	}

	@Override
	@Transactional
	public void addMember(final Long workspaceId, final String username) {
		final User user = userRepository.read(username);
		final Workspace workspace = workspaceRepository.read(workspaceId);
		
		// add member to workspace
		if (!workspace.getMembers().contains(user)) {
			workspace.addMember(user);
			workspaceRepository.save(workspace);
		}		
	}

	@Override
	@Transactional
	public void removeMember(final Long workspaceId, final String username) {
		final User user = userRepository.read(username);
		final Workspace workspace = workspaceRepository.read(workspaceId);
		
		// add member to workspace
		if (workspace.getMembers().contains(user)) {
			workspace.removeMember(user);
			workspaceRepository.save(workspace);
		}		
	}

	@Override
	public List<WorkspaceDTO> findByOwner(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByOwnerUsername(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}	
	
	@Override
	public List<WorkspaceDTO> findByMember(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByMemberUsername(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}

	@Override
	public List<WorkspaceDTO> findAllMyWorkspaces(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByMemberOrOwner(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}

	@Override
	public List<WorkspaceDTO> findByType(final WorkspaceQueryType queryType) {
		List<Workspace> workspaces ;
		final String username = SecurityContextHolder.getContext().getAuthentication().getName();
		switch (queryType) {
			case OWNER:
				workspaces = workspaceRepository.findByOwnerUsername(username);
				break;
			case MEMBER:
				workspaces = workspaceRepository.findByMemberUsername(username);
				break;
			case ALL_MY:
				workspaces = workspaceRepository.findByMemberUsername(username);
				break;
			default:
				workspaces = new ArrayList<Workspace>();
		}
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}
}
