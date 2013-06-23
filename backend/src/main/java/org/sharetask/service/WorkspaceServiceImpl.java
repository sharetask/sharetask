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

import org.sharetask.api.Constants;
import org.sharetask.api.InvitationService;
import org.sharetask.api.WorkspaceQueryType;
import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.InvitationDTO;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.entity.User;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;
import org.sharetask.utility.DTOConverter;
import org.sharetask.utility.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementati0on of workspace service.
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

	@Inject 
	private InvitationService invitationService; 

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#create(org.sharetask.api.dto.WorkspaceDTO)
	 */
	@Override
	@Transactional
	public WorkspaceDTO create(final WorkspaceDTO workspace) {
		final Workspace workspaceEntity = DTOConverter.convert(workspace, Workspace.class);
		// store owner of workspace
		final User user = userRepository.read(SecurityUtil.getCurrentSignedInUsername());
		workspaceEntity.setOwner(user);
		// add member to workspace
		workspaceEntity.addMember(user);
		// store workspace
		final Workspace storedWorkspaceEntity = workspaceRepository.save(workspaceEntity);
		return DTOConverter.convert(storedWorkspaceEntity, WorkspaceDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#update(org.sharetask.api.dto.WorkspaceDTO)
	 */
	@Override
	@Transactional
	@PreAuthorize("isAuthenticated() and hasPermission(#workspace.id, 'isWorkspaceOwner')")
	public WorkspaceDTO update(final WorkspaceDTO workspace) {
		final Workspace workspaceEntity = workspaceRepository.read(workspace.getId());
		DTOConverter.convert(workspace, workspaceEntity);
		final Workspace storedWorkspaceEntity = workspaceRepository.save(workspaceEntity);
		return DTOConverter.convert(storedWorkspaceEntity, WorkspaceDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#addMember(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public void addMember(final String invitationCode) {
		final InvitationDTO invitation = invitationService.confirmInvitation(invitationCode);
		final Workspace workspace = workspaceRepository.read(invitation.getEntityId());
		final User user = userRepository.read(invitation.getEmail());

		// add member to workspace
		if (!workspace.getMembers().contains(user.getUsername())) {
			workspace.addMember(user);
			workspaceRepository.save(workspace);
		}
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#removeMember(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERIMISSION_WORKSPACE_OWNER)
	public void removeMember(final Long workspaceId, final String username) {
		final User user = userRepository.read(username);
		final Workspace workspace = workspaceRepository.read(workspaceId);

		// add member to workspace
		if (workspace.getMembers().contains(user)) {
			workspace.removeMember(user);
			workspaceRepository.save(workspace);
		}
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#findByOwner(java.lang.String)
	 */
	@Override
	public List<WorkspaceDTO> findByOwner(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByOwnerUsername(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#findByMember(java.lang.String)
	 */
	@Override
	public List<WorkspaceDTO> findByMember(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByMemberUsername(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#findAllMyWorkspaces(java.lang.String)
	 */
	@Override
	public List<WorkspaceDTO> findAllMyWorkspaces(final String username) {
		final List<Workspace> workspaces = workspaceRepository.findByMemberOrOwner(username);
		return DTOConverter.convertList(workspaces, WorkspaceDTO.class);
	}

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#findByType(org.sharetask.api.WorkspaceQueryType)
	 */
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

	/* (non-Javadoc)
	 * @see org.sharetask.api.WorkspaceService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	@PreAuthorize(Constants.PERIMISSION_WORKSPACE_OWNER)
	public void delete(final Long workspaceId) {
		workspaceRepository.delete(workspaceId);
	}
}
