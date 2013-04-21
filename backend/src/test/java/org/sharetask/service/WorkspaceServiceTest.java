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
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.UserDTO;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Workspace;
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
	private UserRepository userRepository;

	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#createWorkspace(org.sharetask.entity.Workspace)}.
	 */
	@Test
	public void testAddWorkspace() {
		WorkspaceDTO workspace = new WorkspaceDTO();
		workspace.setTitle("Test");
		UserDTO userDTO = new UserDTO();
		userDTO.setId(100L);
		workspace.setOwner(userDTO);
		Collection<UserDTO> members = new ArrayList<UserDTO>();
		userDTO = new UserDTO();
		userDTO.setId(101L);
		members.add(userDTO);
		workspace.setMembers(members);
		WorkspaceDTO dto = workspaceService.createWorkspace(workspace);
		Assert.assertThat(dto.getTitle(), CoreMatchers.is("Test"));
		Assert.assertThat(dto.getDescription(), CoreMatchers.nullValue());
		Assert.assertThat(dto.getOwner().getId(), CoreMatchers.is(100L));
		Assert.assertThat(dto.getMembers().size(), CoreMatchers.is(1));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#addMemeber(Long, Long)}.
	 */
	@Test
	public void testAddMemeber() {
		workspaceService.addMemeber(100L, 101L);
		Workspace workspace = workspaceRepository.findOne(100L);
		Assert.assertThat(workspace.getMemebers().size(), CoreMatchers.is(3));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#removeMemeber(Long, Long)}.
	 */
	@Test
	public void testRemoveMemeber() {
		workspaceService.removeMemeber(100L, 102L);
		Workspace workspace = workspaceRepository.findOne(100L);
		Assert.assertThat(workspace.getMemebers().size(), CoreMatchers.is(1));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.WorkspaceService#findWorkspaceByOwner(Long)}.
	 */
	@Test
	public void testFindWorkspaceByOwner() {
		List<WorkspaceDTO> workspaces = workspaceService.findWorkspaceByOwner(100L);
		Assert.assertThat(workspaces.size(), CoreMatchers.is(1));
		Assert.assertThat(workspaces.get(0).getId(), CoreMatchers.is(100L));
	}
}
