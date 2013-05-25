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
	private UserInfoDTO createdBy;

	private Date createdOn;

	@Getter	@Setter
	private UserInfoDTO updatedBy;

	private Date updatedOn;

	private Date dueDate;

	@Getter	@Setter
	private String priority;

	@Getter	@Setter
	private List<String> tags;

	@Getter	@Setter
	private String state;
	
	@Getter	@Setter
	private UserInfoDTO assignee;

	@Getter @Setter
	private Integer eventsCount;
	
	@Getter @Setter
	private Integer commentsCount;
	
	public Date getCreatedOn() {
		return this.createdOn == null ? null : (Date)this.createdOn.clone();
	}
	
	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn == null ? null : (Date)createdOn.clone();
	}
	
	public Date getUpdatedOn() {
		return this.updatedOn == null ? null : (Date)this.updatedOn.clone();
	}
	
	public void setUpdatedOn(final Date updatedOn) {
		this.updatedOn = updatedOn == null ? null : (Date)updatedOn.clone();
	}
	
	public Date getDueDate() {
		return this.dueDate == null ? null : (Date)this.dueDate.clone();
	}
	
	public void setDueDate(final Date dueDate) {
		this.dueDate = dueDate == null ? null : (Date)dueDate.clone();
	}
}
