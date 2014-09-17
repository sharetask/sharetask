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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.sharetask.entity.Role;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString(exclude = "password")
public class UserDTO {

	@NotBlank
	@Email
	@Size(min = 6, max = 255)
	@Getter @Setter
	private String username;
	
	@NotBlank
	@Size(min = 8, max = 255)
	@Getter @Setter
	private String password;
	
	@Getter @Setter
	private Boolean enabled = Boolean.FALSE;

	@NotBlank
	@Size(min = 1, max = 255)
	@Getter @Setter
	private String name;
	
	@NotBlank
	@Size(min = 1, max = 255)
	@Getter @Setter
	private String surName;
	
	@Size(min = 4, max = 20)
	@Getter @Setter
	private String mobilePhone;
	
	@Size(min = 2, max = 10)
	@Getter @Setter
	private String language;

	@Getter
	private final Collection<Role> roles = new ArrayList<Role>();
	
	private Date createdOn;
	
	private Date updatedOn;
	
	public void addRole(final Role role) {
		roles.add(role);
	}
	
	public Date getCreatedOn() {
		return createdOn == null ? null : (Date)createdOn.clone();
	}
	
	public Date getUpdatedOn() {
		return updatedOn == null ? null : (Date)updatedOn.clone();
	}
	
	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn == null ? null : (Date)createdOn.clone();
	}
	
	public void setUpdatedOn(final Date updatedOn) {
		this.updatedOn = updatedOn == null ? null : (Date)updatedOn.clone();
	}
}
