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
package org.sharetask.utility.converter;

import org.dozer.CustomConverter;
import org.dozer.MappingException;
import org.sharetask.api.dto.TaskDTO;
import org.sharetask.api.dto.UserInfoDTO;
import org.sharetask.entity.Task;
import org.sharetask.entity.Task.PriorityType;
import org.sharetask.entity.User;
import org.sharetask.utility.DTOConverter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
public class TaskConverter implements CustomConverter {

	/* (non-Javadoc)
	 * @see org.dozer.CustomConverter#convert(java.lang.Object, java.lang.Object, java.lang.Class, java.lang.Class)
	 */
	@Override
	public Object convert(final Object destination, final Object source, final Class<?> destClass, 
	                      final Class<?> sourceClass) {
		Object result = null;
		if (source instanceof TaskDTO) {
			final Task task = convert((TaskDTO) source, (Task) destination);
			result = task;
		} else if (source instanceof Task) {
			final TaskDTO taskDTO = convert((Task) source);
			result = taskDTO;
		} else if (source != null) {
			throw new MappingException("Converter TaskConverter used incorrectly. Arguments passed were:"
					+ destination + " and " + source);
		}
		return result;
	}

	private TaskDTO convert(final Task source) {
		final TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(source.getId());
		taskDTO.setTitle(source.getTitle());
		taskDTO.setDescription(source.getDescription());
		taskDTO.setCreatedBy(DTOConverter.convert(source.getCreatedBy(), UserInfoDTO.class));
		taskDTO.setCreatedOn(source.getCreatedOn());
		taskDTO.setUpdatedBy(DTOConverter.convert(source.getUpdatedBy(), UserInfoDTO.class));
		taskDTO.setUpdatedOn(source.getUpdatedOn());
		taskDTO.setDueDate(source.getDueDate());
		taskDTO.setPriority(source.getPriority().name());
		taskDTO.setTags(source.getTags());
		taskDTO.setState(source.getState().name());
		if (source.getAssignee() != null) {
			final User assignee = source.getAssignee();
			taskDTO.setAssignee(DTOConverter.convert(assignee, UserInfoDTO.class));
		}
		taskDTO.setEventsCount(source.getEvents().size());
		taskDTO.setCommentsCount(source.getComments().size());
		return taskDTO;
	}

	private Task convert(final TaskDTO sourceDTO, final Task destination) {
		final Task task = destination == null ? new Task() : destination;
		task.setTitle(sourceDTO.getTitle());
		task.setDescription(sourceDTO.getDescription());
		task.setDueDate(sourceDTO.getDueDate());
		task.setPriority(PriorityType.valueOf(sourceDTO.getPriority()));
		task.setTags(sourceDTO.getTags());
		return task;
	}
}
