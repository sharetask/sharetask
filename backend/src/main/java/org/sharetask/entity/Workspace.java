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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "WORKSPACE")
@NamedQueries(value = { 
		@NamedQuery(name = Workspace.QUERY_NAME_FIND_BY_OWNER_USERNAME, query = Workspace.QUERY_FIND_BY_OWNER_USERNAME),   
		@NamedQuery(name = Workspace.QUERY_NAME_FIND_BY_MEMBER_USERNAME, query = Workspace.QUERY_FIND_BY_MEMBER_USERNAME),  
		@NamedQuery(name = Workspace.QUERY_NAME_FIND_BY_MEMBER_OR_OWNER, query = Workspace.QUERY_FIND_BY_MEMBER_OR_OWNER)  
	})
public class Workspace extends BaseEntity implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	public static final String QUERY_NAME_FIND_BY_OWNER_USERNAME = "Workspace.findByOwnerUsername";
	public static final String QUERY_FIND_BY_OWNER_USERNAME = "SELECT w FROM Workspace w WHERE w.owner.username = :ownerUserName";

	public static final String QUERY_NAME_FIND_BY_MEMBER_USERNAME = "Workspace.findByMemberUsername";
	public static final String QUERY_FIND_BY_MEMBER_USERNAME = "SELECT w FROM Workspace w, IN(w.members) as m WHERE m.username = :memberUsername";

	public static final String QUERY_NAME_FIND_BY_MEMBER_OR_OWNER = "Workspace.findByMemberOrOwner";
	public static final String QUERY_FIND_BY_MEMBER_OR_OWNER = "SELECT w FROM Workspace w LEFT OUTER JOIN w.members as m "
			+ "WHERE m.username = :username or w.owner.username = :username";

	@Getter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter	@Setter
	@Column(name = "TITLE", nullable = false, length = 100)
	private String title;

	@Getter	@Setter
	@Column(name = "DESCRIPTION", nullable = true, length = 256)
	private String description;

	@Getter	@Setter
	@ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
	@JoinColumn(name = "OWNER_USER_NAME")
	private UserInformation owner;

	@OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL)
	private final List<Task> tasks = new ArrayList<Task>();

	@OneToMany
	@JoinTable(name = "WORKSPACE_MEMBER", 
		joinColumns = { @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "ID") }, 
		inverseJoinColumns = { @JoinColumn(name = "USER_NAME", referencedColumnName = "USER_NAME", unique = true) })
	private List<UserInformation> members = new ArrayList<UserInformation>();
	
	public Workspace() {
		members = new ArrayList<UserInformation>();
	}
	
	public Collection<Task> getTasks() {
		return Collections.unmodifiableCollection(tasks);
	}

	public Collection<UserInformation> getMembers() {
		return Collections.unmodifiableCollection(members);
	}

	public void addMember(final UserInformation user) {
		members.add(user);
	}
	
	public void removeMember(final UserInformation user) {
		members.remove(user);
	}
	
	public void clearMembers() {
		members.clear();
	}
}
