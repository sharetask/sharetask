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

import org.sharetask.api.Constants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "WORKSPACE")
@NamedQueries(value = { 
		@NamedQuery(name = Workspace.QUERY_NAME_FIND_BY_OWNER_USERNAME, query = Workspace.QUERY_FIND_BY_OWNER_USERNAME)  
	})
public class Workspace extends BaseEntity implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;

	public static final String QUERY_NAME_FIND_BY_OWNER_USERNAME = "Workspace.findByOwnerId";
	public static final String QUERY_FIND_BY_OWNER_USERNAME = "SELECT w FROM Workspace w WHERE w.owner.username = :ownerUserName";

	@Getter
	@Id	@GeneratedValue(strategy = GenerationType.AUTO)
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
	private User owner;

	@OneToMany(mappedBy = "workspace")
	private List<Task> tasks = new ArrayList<Task>();
	
	@OneToMany
	@JoinTable(name = "WORKSPACE_MEMBER", 
	           joinColumns = { @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "ID") }, 
	           inverseJoinColumns = { @JoinColumn(name = "USER_NAME", referencedColumnName = "USER_NAME", unique = true) })
	private List<User> members = new ArrayList<User>();
	
	public Workspace() {
		this.members = new ArrayList<User>();
	}
	
	public Collection<Task> getTasks() {
		return Collections.unmodifiableCollection(this.tasks);
	}

	public Collection<User> getMemebers() {
		return Collections.unmodifiableCollection(this.members);
	}

	public void addMember(final User user) {
		this.members.add(user);
	}
	
	public void removeMember(final User user) {
		this.members.remove(user);
	}
	
	public void clearMembers() {
		this.members.clear();
	}
}
