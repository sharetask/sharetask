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
	
	@RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody WorkspaceDTO create(@RequestBody final WorkspaceDTO workspace) {
 		return workspaceService.createWorkspace(workspace);
	}
	
	@RequestMapping(value = "/{workspaceId}/member", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void addMemeber(@PathVariable("workspaceId") final Long workspaceId, 
	                       @RequestBody final User user) {
 		workspaceService.addMemeber(workspaceId, user.getUserId());
	}
	
	@RequestMapping(value = "/{workspaceId}/member/{userId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void removeMemeber(@PathVariable("workspaceId") final Long workspaceId, 
	                          @PathVariable("userId") final Long userId) {
 		workspaceService.removeMemeber(workspaceId, userId);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<WorkspaceDTO> findWorkspaceByOwner() {
		// TODO: change to something like this SecurtyContextHolder.getPrincipal()...
 		return workspaceService.findWorkspaceByOwner(1L);
	}
}
