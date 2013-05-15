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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.sharetask.api.Constants;
import org.sharetask.entity.Event.EventType;

import com.google.common.collect.ImmutableList;

/**
 * @author Michal Bocek
 * @since 1.0.0
 */
@ToString
@Entity
@Table(name = "TASK")
@NamedQueries(value = { 
		@NamedQuery(name = Task.QUERY_NAME_FIND_BY_DUE_DATE, query = Task.QUERY_FIND_BY_DUE_DATE)  
	})
public class Task extends BaseEntity implements Serializable {

	private static final long serialVersionUID = Constants.VERSION;
	
	public static final String QUERY_NAME_FIND_BY_DUE_DATE = "Task.findByDueDate";
	public static final String QUERY_FIND_BY_DUE_DATE = "SELECT t FROM Task t WHERE t.dueDate = :dueDate";

	public static enum StateType {
		NEW, FORWARDED, FINISHED;
	}
	
	public static enum PriorityType {
		LOW, MEDIUM, HIGH;
	}
	
	@Getter @Setter
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter @Setter
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "WORKSPACE_ID")
	private Workspace workspace;
	
	@Getter	@Setter
	@Column(name = "TITLE", nullable = false, length = 100)
	private String title;

	@Getter	@Setter
	@Column(name = "DESCRIPTION", nullable = true, length = 256)
	private String description;
	
	@Getter @Setter
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DUE_DATE")
	private Date dueDate;
	
	@Getter @Setter
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ASSIGNEE_USER_NAME")
	private User assignee;
	
	@Getter @Setter
	@Enumerated(value = EnumType.STRING)
	private StateType state;
	
	@Getter @Setter
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "TASK_TAG", joinColumns = @JoinColumn(name = "TASK_ID"))
	@Column(name = "TAG", nullable = false)
	private List<String> tags;

	@Getter @Setter
	@Enumerated(value = EnumType.STRING)
	private PriorityType priority;
	
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="TASK_ID", referencedColumnName="ID", nullable = false)
	private List<Event> events = new ArrayList<Event>();

	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="TASK_ID", referencedColumnName="ID", nullable = false)
	private List<Comment> comments = new ArrayList<Comment>();

	public Task() {
		this.setState(StateType.NEW);
		this.addEvent(new Event(EventType.TASK_CREATED));
	}
	
	public Collection<Event> getEvents() {
		return Collections.unmodifiableCollection(this.events);
	}

	public void addEvent(final Event event) {
		this.events.add(event);
	}

	public List<Comment> getComments() {
		return ImmutableList.copyOf(this.comments);
	}

	public void addComment(final Comment comment) {
		this.comments.add(comment);
	}

	public void finish() {
		this.setState(StateType.FINISHED);
		this.addEvent(new Event(EventType.TASK_FINISHED));
	}
}
