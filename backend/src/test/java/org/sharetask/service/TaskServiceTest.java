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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.sharetask.api.TaskQueue;
import org.sharetask.api.TaskService;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.data.DbUnitTest;
import org.sharetask.entity.Event;
import org.sharetask.entity.Event.EventType;
import org.sharetask.entity.Task;
import org.sharetask.entity.Task.StateType;
import org.sharetask.entity.User;
import org.sharetask.repository.TaskRepository;
import org.sharetask.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TaskServiceTest extends DbUnitTest {
	
	@Inject
	private TaskService taskService;

	@Inject
	private TaskRepository taskRepository;

	/**
	 * Test method for {@link org.sharetask.api.TaskService#createTask(org.sharetask.api.dto.TaskDTO)}.
	 */
	@Test
	public void testCreateTask() {
		TaskDTO task = new TaskDTO();
		task.setTitle("title");
		task.setDescription("description");
		task.setDueDate(new Date());
		task.setPriority("MEDIUM");
		TaskDTO result = taskService.createTask(100L, task);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertThat(result.getTitle(), equalTo("title"));
		assertThat(result.getDescription(), equalTo("description"));
		assertNotNull(result.getDueDate());
		assertNotNull(result.getCreatedBy());
		assertNotNull(result.getCreatedOn());
		assertNotNull(result.getUpdatedBy());
		assertNotNull(result.getUpdatedOn());
		assertThat(result.getPriority(), equalTo("MEDIUM"));
		assertThat(result.getEvents().size(), equalTo(1));
		assertThat(result.getState(), equalTo("NEW"));
		assertTrue(result.getTags() == null);
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#createTask(org.sharetask.api.dto.TaskDTO)}.
	 */
	@Test
	public void testCreateTaskWithoutDueDate() {
		TaskDTO task = new TaskDTO();
		task.setTitle("title");
		task.setDescription("description");
		task.setPriority("MEDIUM");
		TaskDTO result = taskService.createTask(100L, task);
		assertNotNull(result);
		assertNotNull(result.getId());
		assertThat(result.getTitle(), equalTo("title"));
		assertThat(result.getDescription(), equalTo("description"));
		assertNotNull(result.getCreatedBy());
		assertNotNull(result.getCreatedOn());
		assertNotNull(result.getUpdatedBy());
		assertNotNull(result.getUpdatedOn());
		assertThat(result.getPriority(), equalTo("MEDIUM"));
		assertThat(result.getEvents().size(), equalTo(1));
		assertThat(result.getState(), equalTo("NEW"));
		assertTrue(result.getTags() == null);
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#addComment(Long, String)}.
	 */
	@Test
	public void testAddComment() {
		Task task = taskRepository.findOne(100L);
		TaskDTO taskDTO = taskService.addComment(task.getId(), "Test message");
		assertThat(taskDTO.getComments().size(), equalTo(2));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#findTaskByQueue(Long, org.sharetask.api.TaskQueue)}.
	 */
	@Test
	public void testFindTaskByQueueAllTasks() {
		List<TaskDTO> tasks = taskService.findTaskByQueue(100L, TaskQueue.ALL);
		assertThat(tasks.size(), equalTo(2));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#findTaskByQueue(Long, org.sharetask.api.TaskQueue)}.
	 */
	@Test
	public void testFindTaskByQueueExpiredTasks() {
		List<TaskDTO> tasks = taskService.findTaskByQueue(100L, TaskQueue.EXPIRED);
		assertThat(tasks.size(), equalTo(2));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#findTaskByQueue(Long, org.sharetask.api.TaskQueue)}.
	 */
	@Test
	public void testFindTaskByQueueHighPriorityTasks() {
		List<TaskDTO> tasks = taskService.findTaskByQueue(100L, TaskQueue.HIGH_PRIORITY);
		assertThat(tasks.size(), equalTo(1));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#findTaskByQueue(Long, org.sharetask.api.TaskQueue)}.
	 */
	@Test
	public void testFindTaskByQueueFinshedTasks() {
		List<TaskDTO> tasks = taskService.findTaskByQueue(100L, TaskQueue.FINISHED);
		assertThat(tasks.size(), equalTo(1));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#findTaskByQueue(Long, org.sharetask.api.TaskQueue)}.
	 */
	@Test
	public void testFindTaskTodayTasks() {
		List<TaskDTO> tasks = taskService.findTaskByQueue(100L, TaskQueue.TODAY);
		assertThat(tasks.size(), equalTo(0));
	}

	/**
	 * Test method for {@link org.sharetask.api.TaskService#updateTask(TaskDTO)}.
	 */
	@Test
	public void testUpdateTask() {
		Task entity = taskRepository.findOne(100L);
		String title = entity.getTitle();
		TaskDTO taskDTO = DTOConverter.convert(entity, TaskDTO.class);
		taskDTO.setTitle("new Title");
		TaskDTO newTask = taskService.updateTask(taskDTO);
		assertThat(newTask.getTitle(), not(equalTo(title)));
		assertThat(newTask.getTitle(), equalTo("new Title"));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.TaskService#completeTask(Long)}.
	 */
	@Test
	public void testCompleteTask() {
		taskService.completeTask(100L);
		Task task = taskRepository.findOne(100L);
		assertThat(task.getState(), equalTo(StateType.FINISHED));
		assertThat(task.getEvents().size(), equalTo(2));
	}
	
	/**
	 * Test method for {@link org.sharetask.api.TaskService#forwardTask(Long, List)}.
	 */
	@Test
	public void testForwardTask() {
		taskService.forwardTask(100L, Arrays.asList(new String[] { "test3@test.com" }));
		Task task = taskRepository.findOne(100L);
		Collection<Event> events = task.getEvents();
		assertThat(events.toArray(new Event[events.size()])[events.size() - 1].getType(),
				equalTo(EventType.TASK_FORWARDED));
		List<User> owners = task.getOwners();
		assertThat(owners.size(), equalTo(1));
		assertThat(owners.get(0).getUsername(), equalTo("test3@test.com"));
	}
}
