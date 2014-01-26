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
public class TaskController {

	@Inject
	private TaskService taskService;

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task", method = RequestMethod.POST, 
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TaskDTO create(@PathVariable("workspaceId") final Long workspaceId,
	                                    @RequestBody final TaskDTO task) {
 		return taskService.create(workspaceId, task);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("taskId") final Long taskId) {
		taskService.delete(taskId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}", method = RequestMethod.GET, 
	                produces = MediaType.APPLICATION_JSON_VALUE)
	public TaskDTO getTask(@PathVariable("taskId") final Long taskId) {
		return taskService.getTask(taskId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}/comment", method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TaskDTO addComment(@PathVariable("taskId") final Long taskId,
	                                        @RequestBody final Comment comment) {
 		return taskService.addComment(taskId, comment.getComment());
	}

	@RequestMapping(value = "/{workspaceId}/task/{taskId}/comment", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<CommentDTO> getComments(@PathVariable("taskId") final Long taskId) {
		return taskService.getComments(taskId);
	}

	@RequestMapping(value = "/{workspaceId}/task/{taskId}/event", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<EventDTO> getEvents(@PathVariable("taskId") final Long taskId) {
		return taskService.getEvents(taskId);
	}

	@RequestMapping(value = "/{workspaceId}/task", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<TaskDTO> findTaskByQueue(@PathVariable("workspaceId") final Long workspaceId,
	                                                   @RequestParam("taskQueue") final String taskQueue) {
 		return taskService.findByQueue(workspaceId, TaskQueue.valueOf(taskQueue));
	}
	
	@RequestMapping(value = "/tasks",  method = RequestMethod.GET, params = {"taskQueue=ALL_MY"}, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<TaskDTO> getAllMyTasks() {
 		return taskService.getAllMyTasks();
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task", method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public TaskDTO update(@RequestBody final TaskDTO task) {
 		return taskService.update(task);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}/complete", method = RequestMethod.POST)
	public void complete(@PathVariable("taskId") final Long taskId) {
 		taskService.complete(taskId);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}/forward", method = RequestMethod.POST)
	public void forward(@PathVariable("taskId") final Long taskId,
	                    @RequestBody final User assignee) {
 		taskService.forward(taskId, assignee.getUsername());
	}
	
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/{workspaceId}/task/{taskId}/renew", method = RequestMethod.POST)
	public void renew(@PathVariable("taskId") final Long taskId) {
 		taskService.renew(taskId);
	}
}
