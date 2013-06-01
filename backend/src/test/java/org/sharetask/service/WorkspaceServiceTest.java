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

import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Invitation;
import org.sharetask.entity.Workspace;
import org.sharetask.repository.InvitationRepository;
import org.sharetask.repository.UserRepository;
import org.sharetask.repository.WorkspaceRepository;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class WorkspaceServiceTest extends DbUnitTest {

	@Inject
	private WorkspaceService workspaceService;

	@Inject
	private WorkspaceRepository workspaceRepository;
	
	@Inject
	private InvitationRepository invitationRepository;
	
	@Inject
	private UserRepository userRepository;

	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#create(org.sharetask.entity.Workspace)}.
	 */
	@Test
	public void testCreateWorkspace() {
		final WorkspaceDTO workspace = new WorkspaceDTO();
		workspace.setTitle("Test");
		final UserInfoDTO userDTO = new UserInfoDTO();
		userDTO.setUsername("test1@test.com");
		workspace.setOwner(userDTO);
		final WorkspaceDTO dto = workspaceService.create(workspace);
		Assert.assertThat(dto.getTitle(), CoreMatchers.is("Test"));
		Assert.assertThat(dto.getDescription(), CoreMatchers.nullValue());
		Assert.assertThat(dto.getOwner().getUsername(), CoreMatchers.is("test1@test.com"));
		Assert.assertThat(dto.getOwner().getName(), CoreMatchers.is("Test1"));
		Assert.assertThat(dto.getOwner().getSurName(), CoreMatchers.is("Test"));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#update(WorkspaceDTO)}.
	 */
	@Test
	public void testUpdateWorkspace() {
		final List<WorkspaceDTO> workspaces = workspaceService.findByOwner("test1@test.com");
		final WorkspaceDTO workspace = workspaces.get(0);
		workspace.setTitle("Test Title");
		final WorkspaceDTO dto = workspaceService.update(workspace);
		Assert.assertThat(dto.getTitle(), CoreMatchers.is("Test Title"));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#addMember(String)}.
	 */
	@Test
	public void testAddMember() {
		workspaceService.addMember("XXXYYY");
		final Workspace workspace = workspaceRepository.findOne(100L);
		Assert.assertThat(workspace.getMembers().size(), CoreMatchers.is(2));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#invite(Long, String)}.
	 */
	@Test
	public void testInvite() {
		workspaceService.invite(100L, "test2@test.com");
		final List<Invitation> findAll = invitationRepository.findAll();
		boolean result = false;
		for (final Invitation invitation : findAll) {
			if (invitation.getUsername().equals("test2@test.com")) {
				result = true;
				Assert.assertThat(invitation.getEntityId(), CoreMatchers.is(100L));
			}
		}
		Assert.assertThat(result, CoreMatchers.is(true));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#removeMember(Long, Long)}.
	 */
	@Test
	public void testRemoveMember() {
		workspaceService.removeMember(100L, "test3@test.com");
		final Workspace workspace = workspaceRepository.findOne(100L);
		Assert.assertThat(workspace.getMembers().size(), CoreMatchers.is(0));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#findByOwner(String)}.
	 */
	@Test
	public void testFindWorkspaceByOwner() {
		final List<WorkspaceDTO> workspaces = workspaceService.findByOwner("test1@test.com");
		Assert.assertThat(workspaces.size(), CoreMatchers.is(1));
		Assert.assertThat(workspaces.get(0).getId(), CoreMatchers.is(100L));
	}

	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#findByMember(String)}.
	 */
	@Test
	public void testFindWorkspaceByMember() {
		final List<WorkspaceDTO> workspaces = workspaceService.findByMember("test3@test.com");
		Assert.assertThat(workspaces.size(), CoreMatchers.is(1));
		Assert.assertThat(workspaces.get(0).getId(), CoreMatchers.is(100L));
	}

	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#findAllMyWorkspaces(String)}.
	 */
	@Test
	public void testFindAllMyWorkspaces() {
		final List<WorkspaceDTO> workspaces = workspaceService.findAllMyWorkspaces("test3@test.com");
		Assert.assertThat(workspaces.size(), CoreMatchers.is(2));
	}
	

	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#delete(Long)}.
	 */
	@Test
	public void testDelete() {
		workspaceService.delete(100L);
		final Workspace workspace = workspaceRepository.findOne(100L);
		assertThat(workspace, CoreMatchers.nullValue());
	}
}
