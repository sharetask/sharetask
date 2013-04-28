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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.Getter;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@MappedSuperclass
public abstract class BaseEntity {

	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;
	
	@Getter
	@Column(name = "CREATED_BY", nullable = false)
	private String createdBy; 

	@Getter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON", nullable = false)
	private Date updatedOn;

	@Getter
	@Column(name = "UPDATED_BY", nullable = false)
	private String updatedBy; 

	@PrePersist
	private void onCreate() {
		this.updatedOn = this.createdOn = new Date();
		// TODO fix user name 
		this.updatedBy = this.createdBy =  SecurityContextHolder.getContext().getAuthentication().getName();
	}

	@PreUpdate
	private void onUpdate() {
		this.updatedOn = new Date();
		// TODO fix user name 
		this.updatedBy = SecurityContextHolder.getContext().getAuthentication().getName();;
	}
}
