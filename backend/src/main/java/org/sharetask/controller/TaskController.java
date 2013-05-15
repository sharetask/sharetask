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

import org.sharetask.api.TaskQueue;
import org.sharetask.api.TaskService;
import org.sharetask.api.dto.CommentDTO;
import org.sharetask.api.dto.EventDTO;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.controller.json.Comment;
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
@RequestMapping("/api/workspace/{workspaceId}/task")
public class TaskController {

	@Inject
	private TaskService taskService;
	
	@RequestMapping(method = RequestMethod.POST, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody TaskDTO create(@PathVariable("workspaceId") final Long workspaceId, 
	                                    @RequestBody final TaskDTO task) {
 		return taskService.createTask(workspaceId, task);
	}
	
	@RequestMapping(value = "/{taskId}/comment", 
	                method = RequestMethod.POST, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody TaskDTO addComment(@PathVariable("taskId") final Long taskId,
	                                        @RequestBody final Comment comment) {
 		return taskService.addComment(taskId, comment.getComment());
	}

	@RequestMapping(value = "/{taskId}/comment", 
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody List<CommentDTO> getComments(@PathVariable("taskId") final Long taskId) {
	return taskService.getComments(taskId);
}

	@RequestMapping(value = "/{taskId}/event", 
            method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE)
public @ResponseBody List<EventDTO> getEvents(@PathVariable("taskId") final Long taskId) {
	return taskService.getEvents(taskId);
}

	@RequestMapping(method = RequestMethod.GET, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<TaskDTO> findTaskByQueue(@PathVariable("workspaceId") final Long workspaceId, 
	                                                   @RequestParam("taskQueue") final String taskQueue) {
 		return taskService.findTaskByQueue(workspaceId, TaskQueue.valueOf(taskQueue));
	}
	
	@RequestMapping(method = RequestMethod.PUT, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody TaskDTO update(@RequestBody final TaskDTO task) {
 		return taskService.updateTask(task);
	}

	@RequestMapping(value = "/{taskId}/complete", 
	                method = RequestMethod.POST, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.OK)
	public void complete(@PathVariable("taskId") final Long taskId) {
 		taskService.completeTask(taskId);
	}	

	@RequestMapping(value = "/{taskId}/forward", 
	                method = RequestMethod.POST, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public void forward(@PathVariable("taskId") final Long taskId,
	                    @RequestBody final String assignee) {
 		taskService.forwardTask(taskId, assignee);
	}	
}
