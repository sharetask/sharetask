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
package org.sharetask.api.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
public class TaskDTO {

	@Getter @Setter
	private Long id;

	@NotNull
	@Getter	@Setter
	private String title;

	@Getter	@Setter
	private String description;

	@Getter	@Setter
	private String createdBy;

	@Getter	@Setter
	private Date createdOn;

	@Getter	@Setter
	private String updatedBy;

	@Getter	@Setter
	private Date updatedOn;

	@Getter	@Setter
	private Date dueDate;

	@Getter	@Setter
	private String priority;

	@Getter	@Setter
	private List<String> tags;

	@Getter	@Setter
	private String state;
	
	@Getter	@Setter
	private UserDTO assignee;

	@Getter @Setter
	private List<EventDTO> events;
	
	@Getter @Setter
	private List<CommentDTO> comments;
}
