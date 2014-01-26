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
package org.sharetask.controller;

import java.util.List;

import javax.inject.Inject;

import org.sharetask.api.InvitationService;
import org.sharetask.api.WorkspaceQueryType;
import org.sharetask.api.WorkspaceService;
import org.sharetask.api.dto.WorkspaceDTO;
import org.sharetask.controller.json.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@Controller
@RequestMapping("/api/workspace")
public class WorkspaceController {

	@Inject
	private WorkspaceService workspaceService;
	
	@Inject
	private InvitationService invitationService;
	
	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public WorkspaceDTO create(@RequestBody final WorkspaceDTO workspace) {
 		return workspaceService.create(workspace);
	}
	
	@RequestMapping(method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody public WorkspaceDTO update(@RequestBody final WorkspaceDTO workspace) {
		return workspaceService.update(workspace);
	}

	@RequestMapping(value = "/{workspaceId}/invite", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void invite(@PathVariable("workspaceId") final Long workspaceId, 
	                   @RequestBody final User user) {
 		invitationService.inviteWorkspaceMember(workspaceId, user.getUsername());
	}

	// delete operation for url with email doesn't work on openshift: don't know why 
	@RequestMapping(value = "/{workspaceId}/member/delete ", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void removeMember(@PathVariable("workspaceId") final Long workspaceId,
			                 @RequestBody final User user) {
 		workspaceService.removeMember(workspaceId, user.getUsername());
	}
	
	@RequestMapping(method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<WorkspaceDTO> findWorkspace(@RequestParam("type") final String type) {
		final WorkspaceQueryType queryType = WorkspaceQueryType.valueOf(type);
 		return workspaceService.findByType(queryType);
	}
	
	@RequestMapping(value = "/{workspaceId}", method = RequestMethod.DELETE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable("workspaceId") final Long workspaceId) {
		workspaceService.delete(workspaceId);
	}	
}
