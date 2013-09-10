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
package org.sharetask.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.sharetask.api.Constants;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "USER_INFORMATION")
@NoArgsConstructor
public class UserInformation implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	@Id
	@Getter @Setter
	@Column(name = "USER_NAME", nullable = false, length = 255)
	private String username;
	
	@Getter @Setter
	@Column(name = "NAME", nullable = false, length = 255)
	private String name;
	
	@Getter @Setter
	@Column(name = "SURNAME", nullable = false, length = 255)
	private String surName;
	
	@Setter
	@ElementCollection(targetClass = Role.class)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_NAME"))
	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Collection<Role> roles = new ArrayList<Role>();;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON", nullable = false)
	private Date updatedOn;

	public UserInformation(final String username) {
		this.username = username;
	}

	public Collection<Role> getRoles() {
		return Collections.unmodifiableCollection(roles);
	}
	
	public Date getCreatedOn() {
		return createdOn == null ? null : (Date)createdOn.clone();
	}
	
	public Date getUpdatedOn() {
		return updatedOn == null ? null : (Date) updatedOn.clone();
	}
	
	@PrePersist
	private void preCreate() {
		createdOn = new Date();
		updatedOn = new Date();
	}

	@PreUpdate
	private void preUpdate() {
		updatedOn = new Date();
	}
}
