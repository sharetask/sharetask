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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
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
@Table(name = "INVITATION")
@NamedQueries(value = { 
		@NamedQuery(name = Invitation.QUERY_NAME_FIND_BY_CODE, query = Invitation.QUERY_FIND_BY_CODE)   
	})
public class Invitation implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	public static final String QUERY_NAME_FIND_BY_CODE = "Invitation.findByInvitationCode";
	public static final String QUERY_FIND_BY_CODE = "SELECT i FROM Invitation i WHERE i.invitationCode = :invitationCode";
	
	public static enum InvitationType {
		ADD_WORKSPACE_MEMBER, USER_REGISTRATION;
	}

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter @Setter
	@Column(name = "USER_NAME", nullable = false, length = 255)
	private String username;

	@Getter @Setter
	@Column(name = "INVITATION_CODE", nullable = false, length = 64)
	private String invitationCode;

	@Getter	@Setter
	@Enumerated(value = EnumType.STRING)
	private InvitationType type;

	@Getter @Setter
	@Column(name = "ENTITY_ID", nullable = false)
	private Long entityId;
	
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_ON", nullable = false)
	private Date createdOn;

	@Getter @Setter
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	public Date getCreatedOn() {
		return createdOn == null ? null : (Date)createdOn.clone();
	}
	
	@PrePersist
	private void preCreate() {
		createdOn = new Date();
	}
}
