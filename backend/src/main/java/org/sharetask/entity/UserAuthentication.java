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
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.sharetask.api.Constants;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "USER_AUTHENTICATION")
public class UserAuthentication implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	@Id
	@Getter @Setter
	@Column(name = "USER_NAME", nullable = false, length = 255)
	private String username;
	
	@Getter @Setter
	@Column(name = "PASSWORD", nullable = false, length = 64)
	private String password;
	
	@Getter @Setter
	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;
	
	@Getter @Setter
	@Column(name = "SALT", nullable = false, length = 64)
	private String salt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_ON", nullable = false)
	private Date updatedOn;

	@Getter @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name="USER_NAME", referencedColumnName="USER_NAME")	
	private UserInformation userInfo;
	
	public UserAuthentication() {
		userInfo = new UserInformation();
	}
	
	public UserAuthentication(final String username) {
		this.username = username;
		userInfo = new UserInformation(username);
	}

	public Date getCreatedOn() {
		return createdOn == null ? null : (Date) createdOn.clone();
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
